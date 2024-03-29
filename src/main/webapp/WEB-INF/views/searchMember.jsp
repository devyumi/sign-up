<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>회원조회</title>

</head>
<body>
<br><br>
<strong>회원조회</strong>
<br><br><br>

    <a href="/home">홈으로</a><p/><p/>

    <table>
        <tr>
            <th>순번</th>
            <th>이메일</th>
            <th>닉네임</th>
        </tr>
            <c:forEach items="${membersList}" var="member" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${member.email}</td>
                    <td>${member.nickname}</td>
                </tr>
            </c:forEach>
        </table>
</body>
</html>