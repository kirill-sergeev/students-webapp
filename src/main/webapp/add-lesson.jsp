<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="group" scope="request" type="com.sergeev.studapp.model.Group"/>
<jsp:useBean id="types" scope="request" type="com.sergeev.studapp.model.Lesson.Type[]"/>
<jsp:useBean id="orders" scope="request" type="com.sergeev.studapp.model.Lesson.Order[]"/>
<jsp:useBean id="courses" scope="request" type="java.util.List<com.sergeev.studapp.model.Course>"/>

<c:set var="title" scope="request" value="Add a new lesson"/>
<jsp:include flush="true" page="partial/header.jsp"/>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-3">
            <form action="${pageContext.request.contextPath}/lesson" method="POST">
                <br>
                <label>Add a new lesson</label>
                <div class="form-group">
                    <input type="hidden" name="action" value="save">
                    <input type="hidden" name="group" class="form-control" value="${group.id}">
                </div>
                <div class="form-group">
                    <label>Discipline</label>
                        <select class="form-control" name="discipline">
                            <c:forEach items="${courses}" var="course">
                                <option value="${course.discipline.id}">${course.discipline.title}</option>
                            </c:forEach>
                        </select>

                </div>
                <div class="form-group">
                    <label>Type</label>
                        <select class="form-control" name="type">
                            <c:forEach items="${types}" var="type">
                                <option value="${type.name()}">${type.name()}</option>
                            </c:forEach>
                        </select>
                </div>
                <div class="form-group">
                    <label>Time</label>
                        <select class="form-control" name="number">
                            <c:forEach items="${orders}" var="order">
                                <option value="${order.ordinal()}">${order.startTime} - ${order.endTime}</option>
                            </c:forEach>
                        </select>
                </div>
                <div class="form-group">
                    <label>Date</label>
                        <input type="date" name="date" class="form-control" value="01-01-2017">
                </div>
                <button type="submit" class="btn btn-block btn-primary">Submit</button>
            </form>
        </div>
    </div>
</div>

<jsp:include flush="true" page="partial/footer.jsp"/>