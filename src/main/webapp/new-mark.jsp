<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="lesson" scope="request" type="com.sergeev.studapp.model.Lesson"/>
<jsp:useBean id="students" scope="request" type="java.util.ArrayList<com.sergeev.studapp.model.Student>"/>

<jsp:include flush="true" page="partial/header.jsp">
    <jsp:param name="title" value="Add a new mark in group ${lesson.course.group} at lesson ${lesson.course.discipline} ${lesson.date}"/>
</jsp:include>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-4">
            <form action="create-mark" method="POST">
                <div class="form-group">
                    <input type="hidden" name="lesson" class="form-control" value="${lesson.id}">
                </div>
                <div class="form-group">
                    <label>Student
                        <select class="form-control" name="student">
                            <c:forEach var="student" items="${students}">
                                <option value="${student.id}">${student.firstName} ${student.lastName}</option>
                            </c:forEach>
                        </select>
                    </label>
                </div>
                <div class="form-group">
                    <input type="text" name="value" class="form-control" placeholder="Value...">
                </div>
                <button type="submit" class="btn btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>