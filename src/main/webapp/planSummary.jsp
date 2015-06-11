<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<head>
<title>Project Plan Summary</title>
<style>
.space {
	width: 10px
}

th.plan, th.act {
	width: 70px
}

td.plan, td.act {
	padding-right: 4px;
	border: 1px solid gray;
	text-align: right
}
</style>
</head>
<body>
	<h1>
		<c:out value="${pdash.projectPath}" />
	</h1>
	<h2>Project Plan Summary</h2>
	<h3>Overall Metrics</h3>
	<table>
		<tr>
			<th></th>
			<th class="space"></th>
			<th class="plan">Plan</th>
			<th class="act">Actual</th>
		</tr>
		<tr>
			<td>Productivity</td>
			<td class="space"></td>
			<td class="plan"><c:out
					value="${pdash.data.string['Estimated Productivity']}" /></td>
			<td class="act"><c:out
					value="${pdash.data.string['Productivity']}" /></td>
		</tr>
		<tr>
			<td>Total Hours</td>
			<td class="space"></td>
			<td class="plan"><fmt:formatNumber maxFractionDigits="1"
					value="${pdash.data['Estimated Time'] / 60}" /></td>
			<td class="act"><fmt:formatNumber maxFractionDigits="1"
					value="${pdash.data['Time'] / 60}" /></td>
		</tr>
		<tr>
			<td>Time Estimating Error</td>
			<td class="space"></td>
			<td></td>
			<td class="act"><c:out
					value="${pdash.data.string['Time Estimating Error']}" /></td>
		</tr>
		<tr>
			<td>CPI</td>
			<td class="space"></td>
			<td></td>
			<td class="act"><c:out value="${pdash.data.string['CPI']}" /></td>
		</tr>
		<tr>
			<td>% Appraisal COQ</td>
			<td class="space"></td>
			<td class="act"><c:out
					value="${pdash.data.string['Estimated % Appraisal COQ']}" /></td>
			<td class="act"><c:out
					value="${pdash.data.string['% Appraisal COQ']}" /></td>
		</tr>
		<tr>
			<td>% Failure COQ</td>
			<td class="space"></td>
			<td class="act"><c:out
					value="${pdash.data.string['Estimated % Failure COQ']}" /></td>
			<td class="act"><c:out
					value="${pdash.data.string['% Failure COQ']}" /></td>
		</tr>
		<tr>
			<td>% Total COQ</td>
			<td class="space"></td>
			<td class="act"><c:out
					value="${pdash.data.string['Estimated % COQ']}" /></td>
			<td class="act"><c:out value="${pdash.data.string['% COQ']}" /></td>
		</tr>
	</table>
	<h3>Phase Data</h3>
	<table>
		<tr>
			<th></th>
			<th class="space"></th>
			<th colspan="2">Time (Hours)</th>
			<th class="space"></th>
			<th colspan="2">Defects Injected</th>
			<th class="space"></th>
			<th colspan="2">Defects Removed</th>
		</tr>
		<tr>
			<th></th>
			<th class="space"></th>
			<th class="plan">Plan</th>
			<th class="act">Actual</th>
			<th class="space"></th>
			<th class="plan">Plan</th>
			<th class="act">Actual</th>
			<th class="space"></th>
			<th class="plan">Plan</th>
			<th class="act">Actual</th>
		</tr>
		<c:forEach items="${pdash.data.list['Phase_List']}" var="phaseName">
			<c:set var="phase" value="${pdash.data.child[phaseName]}" />
			<tr>
				<td><c:out value="${phaseName}" /></td>
				<td class="space"></td>
				<td class="plan"><fmt:formatNumber maxFractionDigits="1"
						value="${phase['Estimated Time'] / 60}" /></td>
				<td class="act"><fmt:formatNumber maxFractionDigits="1"
						value="${phase['Time'] / 60}" /></td>
				<td class="space"></td>
				<td class="plan"><c:out
						value="${phase.string['Estimated Defects Injected']}" /></td>
				<td class="act"><c:out
						value="${phase.string['Defects Injected']}" /></td>
				<td class="space"></td>
				<td class="plan"><c:out
						value="${phase.string['Estimated Defects Removed']}" /></td>
				<td class="act"><c:out
						value="${phase.string['Defects Removed']}" /></td>
			</tr>
		</c:forEach>
		<tr>
			<td>Total</td>
			<td class="space"></td>
			<td class="plan"><fmt:formatNumber maxFractionDigits="1"
					value="${pdash.data['Estimated Time'] / 60}" /></td>
			<td class="act"><fmt:formatNumber maxFractionDigits="1"
					value="${pdash.data['Time'] / 60}" /></td>
			<td class="space"></td>
			<td class="plan"><c:out
					value="${pdash.data.string['Estimated Defects Injected']}" /></td>
			<td class="act"><c:out
					value="${pdash.data.string['Defects Injected']}" /></td>
			<td class="space"></td>
			<td class="plan"><c:out
					value="${pdash.data.string['Estimated Defects Removed']}" /></td>
			<td class="act"><c:out
					value="${pdash.data.string['Defects Removed']}" /></td>
		</tr>
	</table>
	<%
	    // including the following line provides one-click export to Excel
	%>
	<a href="/reports/excel.iqy?fullPage">Export to Excel</a>
</body>
</html>
