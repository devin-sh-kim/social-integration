<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Login</title>

    <jsp:include page="common/head.jsp"/>

</head>
<body>
<header>
    <jsp:include page="common/nav.jsp"/>
</header>
<main class="container">
    <div class="row justify-content-center">
        <div class="col-4">
            <h1>Login</h1>
            <div class="card">
                <div class="card-body">
                    <div>
                        <form action="login" method="post">
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" id="email" name="email">
                            </div>
                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" class="form-control" id="password" name="password">
                            </div>

                            <c:if test="${error != null and not empty error}">
                                <p class="text-danger">
                                        ${error}
                                </p>
                            </c:if>

                            <button type="submit" class="btn btn-block btn-primary btn-email">Login with Email</button>
                        </form>
                        <div class="row mt-1">
                            <div class="col-6">
                                <a href="reset-password">
                                    Reset Password
                                </a>
                            </div>
                            <div class="col-6 text-right">
                                <a href="/join">
                                    Join
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="text-center my-2">or</div>

                    <a class="btn btn-block btn-kakao" href="${kakaoOAuthUrl}">Login with KAKAO</a>
                    <a class="btn btn-block btn-naver" href="${naverOAuthUrl}">Login with NAVER</a>
                    <a class="btn btn-block btn-facebook" href="${facebookOAuthUrl}">Login with FACEBOOK</a>
                    <a class="btn btn-block btn-google" href="${googleOAuthUrl}">Login with GOOGLE</a>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="common/script.jsp"/>
</body>
</html>