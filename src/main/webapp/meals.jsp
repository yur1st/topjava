<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="meals.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <caption>Meals list</caption>
    <thead>
    <tr>
        <th scope="col">Description</th>
        <th scope="col">Date and Time</th>
        <th scope="col">Calories</th>
        <th scope="col" colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:set var="count" value="1"/>
    <c:forEach items="${meals}" var="meal">
        <c:set var="excess" value="${(meal.excess) ? 'excess' : 'normal'}"/>
        <tr class="${excess}">
            <td>${meal.description}</td>
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
            <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}"/></td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
