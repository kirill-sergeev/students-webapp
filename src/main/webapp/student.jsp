<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="student" scope="request" type="com.sergeev.studapp.model.User"/>
<jsp:useBean id="coursesMarks" scope="request"
             type="java.util.LinkedHashMap<com.sergeev.studapp.model.Course, java.lang.Double>"/>

<c:set var="title" scope="request" value="Student - ${student.firstName} ${student.lastName}"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-8">
            <c:if test="${not empty student}">
                <br>
                <h4>${student.firstName} ${student.lastName} from group
                    <a href="${pageContext.request.contextPath}/group/${student.group.id}">${student.group.title}</a>
                </h4>
                <br>
                <h4>Courses</h4>
                <c:choose>
                    <c:when test="${empty coursesMarks}">
                        <div class="alert alert-warning text-center" role="alert">
                            <strong>No courses!</strong>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-hover table-sm">
                            <thead>
                            <tr>
                                <th>Discipline</th>
                                <th>Avg. mark</th>
                                <th>Marks</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${coursesMarks}">
                                <tr>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/discipline/${item.key.discipline.id}">${item.key.discipline.title}</a>
                                    </td>
                                    <td>${item.value}</td>
                                    <td>
                                        <button class="btn btn-info btn-sm" type="button"
                                                onclick="location.href='${pageContext.request.contextPath}/mark/student/${student.id}/discipline/${item.key.discipline.id}'">
                                            All marks
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>