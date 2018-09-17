<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table border="2" width="2" cellspacing="2" cellpadding="2" >
    <thead>
    <tr>
        <th>№</th>
        <th>ИД</th>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Превышение</th>
        <th>Операции</th>
    </tr>
    </thead>
    <tbody>

    <%--@elvariable id="meals" type="java.util.List<MealWithExceed>"--%>
    <c:forEach items="${meals}" var="meal" varStatus="loop">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <tr style="color: ${meal.exceed ? 'red' : 'green'}" >
            <td><c:out value="${loop.index+1}"/></td>
            <td><c:out value="${meal.id}"/></td>
            <td><%= TimeUtil.dateToString(meal.getDateTime()) %></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><c:out value="${meal.exceed}"/></td>
            <td><a href="addmeal">Добавить</a> <a href="editmeal">Редактировать</a> <a href="deletemeal">Удалить</a> </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>