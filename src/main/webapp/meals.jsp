<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<!DOCTYPE>
<html lang="ru">
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    Вы вошли как пользователь:<b> ${authUser} </b>
    <br>
    <br>
    <div>
        <form method="get" action="meals">
            <div class="row">
                <input hidden name="action" id="action" value="filter">
                <div>
                    <label for="startDate">От даты (включая)</label>
                    <input type="date" name="startDate" id="startDate" value="${param['startDate']}">
                </div>
                <div>
                    <label for="endDate">До даты (включая)</label>
                    <input type="date" name="endDate" id="endDate" value="${param['endDate']}">
                </div>
                <div>
                    <label for="startTime">От времени (включая)</label>
                    <input type="time" name="startTime" id="startTime" value="${param['startTime']}">
                </div>
                <div>
                    <label for="endTime">До времени (исключая)</label>
                    <input type="time" name="endTime" id="endTime" value="${param['endTime']}">
                </div>
            </div>
            <button type="submit">
                Отфильтровать
            </button>
        </form>
    </div>
    <br>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <caption hidden>MealsTable</caption>
        <thead>
        <tr>
            <th scope="col">Date</th>
            <th scope="col">Description</th>
            <th scope="col">Calories</th>
            <th scope="col"></th>
            <th scope="col"></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>