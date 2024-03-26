<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>로그인</title>

</head>
<body>
<div>
    <br><br>
    <strong>로그인</strong>
    <br><br><br>

    <form name="signinForm" action="/signin" method="post" onsubmit="checkPassword()">
        <c:if test="${!empty errorMessage}">
            <script type="text/javascript">
                const message = "${errorMessage}";
                swal(message);
            </script>
        </c:if>

        <table>
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
</body>
</html>