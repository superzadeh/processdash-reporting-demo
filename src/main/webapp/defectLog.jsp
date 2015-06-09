<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Defect Log Query</title>
<style>
table {
	empty-cells: show;
}
</style>
</head>
<body>
	<h1>
		<c:out value="${pdash.projectPath}" />
	</h1>
	<h2>Defect Log Query</h2>
	<table border>
		<tr>
			<th>Project/Task</th>
			<th>Date</th>
			<th>Type</th>
			<th>Injected</th>
			<th>Removed</th>
			<th>Fix Time</th>
			<th>Fix Count</th>
			<th>Pending</th>
			<th>Description</th>
		</tr>
		<c:forEach var="defect"
			items="${pdash.query['from DefectLogFact as d order by d.foundDate']}">
			<tr>
				<td><c:out
						value="/${defect.planItem.project}/${defect.planItem.wbsElement}" /></td>
				<td><c:out value="${defect.foundDateDim}" /></td>
				<td><c:out value="${defect.defectType}" /></td>
				<td><c:out value="${defect.injectedPhase.shortName}" /></td>
				<td><c:out value="${defect.removedPhase.shortName}" /></td>
				<td align="right"><c:out value="${defect.fixTimeMin}" /></td>
				<td align="center"><c:out value="${defect.fixCount}" /></td>
				<td align="center"><c:if test="${defect.fixPending}">*</c:if></td>
				<td><c:out value="${defect.description}" /></td>
			</tr>
		</c:forEach>
	</table>
	<%
		// including the following line provides one-click export to Excel
	%>
	<a href="/reports/excel.iqy">Export to Excel</a>
</body>
</html>
