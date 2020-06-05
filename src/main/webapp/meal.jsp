<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meal Edit/Insert</title>
</head>
<body>
<form method="POST" action='meals' name="frmAdd">
    <c:if test="${param.action != 'insert'}">
        Meal ID : <c:out value="${meal.id}" /> <br/>
    </c:if>
    Description : <input
        type="text" name="description" size="70"
        value="<c:out value="${meal.description}" />"/> <br/>
    Date and Time : <input
        type="datetime-local" name="dateTime"
        value="${meal.dateTime}" />"/> <br/>
    Calories : <input
        type="number" min="1" name="calories" size="10"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>