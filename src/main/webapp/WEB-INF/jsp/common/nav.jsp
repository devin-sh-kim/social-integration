<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-light bg-light">
    <div class="nav">
        <a class="nav-link active" href="/">Home</a>
        <c:choose>
            <c:when test="${sessionScope.LOGIN_ID != null && sessionScope.LOGIN_ID != ''}">
                <a class="nav-link" href="/my-page">My Page</a>
                <a class="nav-link" href="/logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a class="nav-link" href="/login">Login</a>
            </c:otherwise>
        </c:choose>


    </div>
</nav>