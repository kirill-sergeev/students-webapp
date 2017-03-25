package com.sergeev.studapp.actions;

import com.sergeev.studapp.model.Account;
import com.sergeev.studapp.model.User;
import com.sergeev.studapp.service.AccountService;
import com.sergeev.studapp.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

    static final String LOGIN_COOKIE = "MY_SESSION_COOKIE";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        final String path = req.getRequestURI().substring(req.getContextPath().length());

        Account account;
        User user = (User) session.getAttribute("user");
        String remember = (String) session.getAttribute("remember");
        Cookie loginCookie = null;

        if (req.getCookies() == null) {
            resp.sendRedirect("/login");
            return;
        }

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(LOGIN_COOKIE)) {
                loginCookie = cookie;
                break;
            }
        }

        if (user == null) {
            if (loginCookie != null && (account = AccountService.readByToken(loginCookie.getValue())) != null) {
                user = UserService.readByAccount(account);
                //datastore.updateTokenLastActivity(loginCookie.getValue());
                req.getSession().setAttribute("user", user);
            }
        }

        if (path.startsWith("/login")) {
            if (user == null) {
                chain.doFilter(request, response);
                return;
            }
            resp.sendRedirect("/");
            return;
        }

        if (user == null) {
            resp.sendRedirect("/login");
        } else {
            if ("remember".equals(remember)) {
                resp.addCookie(loginCookie);
            }
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
