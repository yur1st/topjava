<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="${pageContext.request.contextPath}/meals">Home</a></h3>
    <hr>
    <h2>${param.containsKey('new') ? 'Create meal' : 'Edit meal'}</h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <sf:form method="post" modelAttribute="meal">
        <sf:input type="hidden" name="id" value="${meal.id}" path="id"/>
        <dl>
            <dt>DateTime:</dt>
            <dd><sf:input type="datetime-local"
                          value="${meal.dateTime}"
                          name="dateTime"
                          path="dateTime"/></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><sf:input type="text"
                          value="${meal.description}"
                          size="40"
                          name="description"
                          path="description"/></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><sf:input type="number"
                          value="${meal.calories}"
                          name="calories"
                          path="calories"/></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </sf:form>
</section>
</body>
</html>
