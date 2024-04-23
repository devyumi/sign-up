<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <style>
        .imgSize{
            width: 200px;
            height: 45px;
        }
    </style>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>로그인</title>

</head>
<body>
<div>
    <br><br>
    <strong>로그인</strong>
    <br><br><br>

    <div>
        <a href="/oauth2/authorization/naver">
            <img src="static/naver_login.png" class="imgSize">
        </a>
    </div>

    <div>
        <a href="/oauth2/authorization/kakao">
            <img src="static/kakao_login.png" class="imgSize">
        </a>
    </div>

    <div>
        <a href="/oauth2/authorization/google">
            <img src="static/google_login.png" class="imgSize">
        </a>
    </div>

    <br>
    <div>
    <form name="signinForm" action="/signin" method="post" onsubmit="checkPassword()">
        <table>
            <tr>
                <span style="color: #ff4238">${errorMessage}</span>
            </tr>
            <tr>
                <td>아이디</td>
                <td>
                    <input type="email" name="username" size="30" required>
                </td>
            </tr>

            <tr>
                <td>비밀번호</td>
                <td>
                    <input type="password" name="password" size="30" required>
                </td>
            </tr>
        </table>
        <br>
        <td>
            <button type="button" onclick="location.href='signup'">회원가입</button>
            <input type="submit" value="로그인">
        </td>
    </form>
    </div>
</div>
</body>
</html>