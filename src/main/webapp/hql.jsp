<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>

<h1>HQL Query</h1>

<form action="hql.jsp" method="post">

Enter an HQL Query:<br/>
<textarea name="q" rows=10 cols=60><c:out value="${param.q}"/></textarea>

<br/><input type="submit" name="run" value="Execute Query"/>

</form>

<c:if test="${not empty param.q}">

<h2>Results</h2>

<c:catch var="queryException">
<c:set var="results" value="${pdash.query[param.q]}"/>
</c:catch>

<c:choose>

<c:when test="${queryException != null}">
<pre><c:out value="${queryException.message}"/></pre>
</c:when>

<c:when test="${empty results}">
<p><i>No items found</i></p>
</c:when>

<c:otherwise>

<table>

<c:forEach items="${results}" var="row">
<tr>

<c:catch var="exception">
<c:forEach items="${row}" var="cell">
<td><c:out value="${cell}"/></td>
</c:forEach>
</c:catch>

<c:if test="${exception != null}">
<td><c:out value="${row}"/></td>
</c:if>

</tr>

</c:forEach>

</table>

</c:otherwise>
</c:choose>

</c:if>

</body>
</html>
