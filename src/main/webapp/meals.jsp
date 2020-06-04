<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>№</th>
        <th>Description</th>
        <th>Date and Time</th>
        <th>Calories</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="count" scope="session" value="1"/>
    <c:forEach items="${meals}" var="meal">
        <c:set var="color" value="${(meal.excess == 'true') ? '#db7093' : '#90ee90'}" />
        <tr bgcolor="${color}">
        <td><c:out value="${count}"/></td>
        <td><c:out value="${meal.description}"/></td>
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
        <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}"/></td>
        <td><c:out value="${meal.calories}"/></td>
        </tr>
        <c:set var="count" value="${count+1}"/>
    </c:forEach>
    </tbody>
</table>
</body>
</html>