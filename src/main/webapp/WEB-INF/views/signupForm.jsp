<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>회원가입</title>

    <script type="text/javascript">
        function checkPassword() {
            if (document.signupForm.password.value !== document.signupForm.checkedPassword.value) {
                alert("비밀번호 일치 여부를 확인하세요.")
                return false;
            }
        }
    </script>

</head>
<body>
<div>
    <br><br>
    <strong>회원가입</strong>
    <br><br><br>

    <form name="signupForm" action="/signup" method="post" onsubmit="checkPassword()">
        <table>
            <tr>
                <td>아이디</td>
                <td>
                    <input type="email" name="email" size="50" placeholder="이메일을 입력해주세요." required>
                    <span style="color: #ff4238">${errorMessage.email}</span>
                </td>
            </tr>

            <tr>
                <td>비밀번호</td>
                <td>
                    <input type="password" name="password" size="50" placeholder="비밀번호를 입력해주세요.(영문, 숫자, 특수기호 포함 8~20자)" required>
                    <span style="color: #ff4238">${errorMessage.password}</span>
                </td>
            </tr>

            <tr>
                <td>비밀번호 확인</td>
                <td>
                    <input type="password" name="checkedPassword" size="50" placeholder="비밀번호를 한번 더 입력해주세요." required>
                </td>
            </tr>

            <tr>
                <td>닉네임</td>
                <td>
                    <input type="text" name="nickname" size="50" placeholder="닉네임을 입력해주세요.(2~10자)" required>
                    <span style="color: #ff4238">${errorMessage.nickname}</span>
                </td>
            </tr>
        </table>
        <br>
        <input type="submit" value="가입완료">
    </form>
</div>
</body>
</html>