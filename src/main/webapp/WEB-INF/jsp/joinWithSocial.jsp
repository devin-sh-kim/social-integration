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

    ${sessionScope.socialAccount}

    <div class="row">
        <div class="col-12">
            <form action="/join-with-social" method="POST">
                <input type="hidden" name="social" value="${sessionScope.socialAccount.social}">
                <input type="hidden" name="socialId" value="${sessionScope.socialAccount.id}">
                <input type="hidden" name="accessToken" value="${sessionScope.socialToken.accessToken}">
                <input type="hidden" name="refreshToken" value="${sessionScope.socialToken.refreshToken}">
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
                        <input type="email" class="form-control" id="email" name="email"
                               value="${sessionScope.socialAccount.email}"
                        ${not empty sessionScope.socialAccount.email ? 'readonly' : ''}
                        >
                    </div>
                </div>
                <div class="form-group row">
                    <label for="name" class="col-sm-2 col-form-label text-right">성명</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="name" name="name"
                               value="${sessionScope.socialAccount.name}"
                        ${not empty sessionScope.socialAccount.name ? 'readonly' : ''}
                        >
                    </div>
                </div>

                <div class="form-group row">
                    <label class="col-sm-2 col-form-label text-right">성별</label>
                    <div class="col-sm-10">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gender-m"
                                   value="M" ${sessionScope.socialAccount.gender eq 'M' ? 'checked' : ''}>
                            <label class="form-check-label" for="gender-m">
                                남성
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="gender" id="gender-f"
                                   value="F" ${sessionScope.socialAccount.gender eq 'F' ? 'checked' : ''}>
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
                            <option value="">연령대를 선택하세요</option>
                            <option value="10" ${sessionScope.socialAccount.age eq '10' ? 'selected' : ''}>10대</option>
                            <option value="20" ${sessionScope.socialAccount.age eq '20' ? 'selected' : ''}>20대</option>
                            <option value="30" ${sessionScope.socialAccount.age eq '30' ? 'selected' : ''}>30대</option>
                            <option value="40" ${sessionScope.socialAccount.age eq '40' ? 'selected' : ''}>40대</option>
                            <option value="50" ${sessionScope.socialAccount.age eq '50' ? 'selected' : ''}>50대</option>
                            <option value="60" ${sessionScope.socialAccount.age eq '60' ? 'selected' : ''}>60대</option>
                            <option value="70" ${sessionScope.socialAccount.age eq '70' ? 'selected' : ''}>70대</option>
                            <option value="80" ${sessionScope.socialAccount.age eq '80' ? 'selected' : ''}>80대 이상
                            </option>
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