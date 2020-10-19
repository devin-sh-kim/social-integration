<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Join</title>

    <jsp:include page="common/head.jsp"/>

</head>
<body>
<header>
    <jsp:include page="common/nav.jsp"/>
</header>
<main class="container">
    <h1>Join</h1>
    <div class="row">
        <div class="col-12">
            <form action="/join" method="POST">
                <div class="form-group row">
                    <label for="terms" class="col-sm-2 col-form-label text-right">약관</label>
                    <div class="col-sm-10">
                        <textarea class="form-control" id="terms" rows="3" readonly>
                            ${terms}
                        </textarea>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="agree-terms" name="agreeTerms">
                            <label class="form-check-label" for="agree-terms">
                                약관 동의
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group row">
                    <label for="email" class="col-sm-2 col-form-label text-right">이메일</label>
                    <div class="col-sm-10">
                        <input type="email" class="form-control" id="email" name="email">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="password" class="col-sm-2 col-form-label text-right">비밀번호</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="password" name="password">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="password-again" class="col-sm-2 col-form-label text-right">비밀번호 확인</label>
                    <div class="col-sm-10">
                        <input type="password" class="form-control" id="password-again">
                    </div>
                </div>
                <div class="form-group row">
                    <label for="name" class="col-sm-2 col-form-label text-right">성명</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="name" name="name">
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 col-form-label text-right">성별</label>
                    <div class="col-sm-10">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gender-m" value="M" checked>
                            <label class="form-check-label" for="gender-m">
                                남성
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gender-f" value="F">
                            <label class="form-check-label" for="gender-f">
                                여성
                            </label>
                        </div>

                    </div>
                </div>

                <div class="form-group row">
                    <label for="age" class="col-sm-2 col-form-label text-right">연령대</label>
                    <div class="col-sm-10">
                        <select class="form-control" name="age" id="age">
                            <option value="" selected>연령대를 선택하세요</option>
                            <option value="10">10대</option>
                            <option value="20">20대</option>
                            <option value="30">30대</option>
                            <option value="40">40대</option>
                            <option value="50">50대</option>
                            <option value="60">60대</option>
                            <option value="70">70대</option>
                            <option value="80">80대 이상</option>
                        </select>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="mobile" class="col-sm-2 col-form-label text-right">휴대폰 연락처</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="mobile" name="mobile">
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-12 text-center">
                        <button type="submit" class="btn btn-primary">가입</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="common/script.jsp"/>
</body>
</html>