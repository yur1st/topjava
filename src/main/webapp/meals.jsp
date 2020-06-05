<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.jsp">Home</a></h3>
<hr>
<h2>Meals</h2>
<style type="text/css">
    TABLE {
        border-collapse: collapse;
        width: 500px;
    }
    TH, TD {
        border: 1px solid black;
        text-align: left;
        vertical-align: center;
        padding: 2px;
    }
    TH {
        height: 15px;
        text-align: center;
        vertical-align: bottom;
        padding: 2px;
    }
</style>
<table border=1>
    <caption>Meals list</caption>
    <thead>
    <tr>
        <th scope="col">№</th>
        <th scope="col">Description</th>
        <th scope="col">Date and Time</th>
        <th scope="col">Calories</th>
        <th scope="col" colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:set var="color" value="${(meal.excess == 'true') ? '#db7093' : '#90ee90'}" />
        <tr bgcolor="${color}">
        <td><c:out value="${meal.id}"/></td>
        <td><c:out value="${meal.description}"/></td>
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
        <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/></td>
        <td><c:out value="${meal.calories}"/></td>
        <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
        <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
