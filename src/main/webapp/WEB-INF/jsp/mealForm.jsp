<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <h2>
        <c:choose>
            <c:when test="${param.containsKey('new')}">
                <spring:message code="editForm.createTitle"/>
            </c:when>
            <c:otherwise>
                <spring:message code="editForm.editTitle"/>
            </c:otherwise>
        </c:choose>
    </h2>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <sf:form method="post" modelAttribute="meal">
        <sf:input type="hidden" name="id" value="${meal.id}" path="id"/>
        <dl>
            <dt><spring:message code="meal.dateTime"/>:</dt>
            <dd><sf:input type="datetime-local"
                          value="${meal.dateTime}"
                          name="dateTime"
                          path="dateTime"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><sf:input type="text"
                          value="${meal.description}"
                          size="40"
                          name="description"
                          path="description"/></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><sf:input type="number"
                          value="${meal.calories}"
                          name="calories"
                          path="calories"/></dd>
        </dl>
        <button type="submit"><spring:message code="common.save"/></button>
        <button onclick="window.history.back()" type="button"><spring:message code="common.cancel"/></button>
    </sf:form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
