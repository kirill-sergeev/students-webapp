package com.sergeev.studapp.actions.student;

import com.sergeev.studapp.dao.DaoFactory;
import com.sergeev.studapp.dao.PersistentException;
import com.sergeev.studapp.model.Group;
import com.sergeev.studapp.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateStudentServlet", urlPatterns = "/create-student")
public class CreateStudentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        Integer groupId = Integer.valueOf(request.getParameter("group"));

        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);

        try {
            Group group = DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getGroupDao().getByPK(groupId);
            student.setGroup(group);
            DaoFactory.getDaoFactory(DaoFactory.POSTGRES).getStudentDao().persist(student);
        } catch (PersistentException e) {
            e.printStackTrace();
        }

        response.sendRedirect("/students");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}