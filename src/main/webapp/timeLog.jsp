<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
<title>Time Log Query</title>
<style>
    table { empty-cells: show; }
</style>
</head>
<body>

<h1><c:out value="${pdash.projectPath}"/></h1>

<h2>Time Log Query</h2>

<table border>

<tr>
<th>Project/Task</th>
<th>Phase</th>
<th>Start Time</th>
<th>Delta</th>
<th>Interrupt</th>
</tr>


<c:set var="processId" value="${pdash.data['Process_ID']}"/>

<c:forEach var="row" items="${pdash.query['
    select t, t.planItem, phase 
    from TimeLogFact as t
    left join t.planItem.phase.mapsToPhase phase
    where phase.process.identifier = ?
    order by t.startDate'][processId]}">

<c:set var="time" value="${row[0]}"/>
<c:set var="planItem" value="${row[1]}"/>
<c:set var="phase" value="${row[2]}"/>

<tr>
<td><c:out value="${planItem}"/></td>
<td><c:out value="${phase.shortName}"/></td>
<td><c:out value="${time.startDate}"/></td>
<td align="right"><fmt:formatNumber value="${time.deltaMin}"/></td>
<td align="right"><c:if test="${time.interruptMin > 0}"><fmt:formatNumber
     value="${time.interruptMin}"/></c:if></td>
</tr>

</c:forEach>

</table>


<% // including the following line provides one-click export to Excel %>
<a href="/reports/excel.iqy">Export to Excel</a>

</body>
</html>
