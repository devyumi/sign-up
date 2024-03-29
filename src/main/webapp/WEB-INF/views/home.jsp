<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>HOME</title>

</head>
<body>
    <br><br>
    <strong>HOME</strong>
    <br><br><br>

    <sec:authorize access="isAnonymous()">
        <a href="/signup">회원가입</a><p/>
        <a href="/signin">로그인</a>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        <a href="/signout">로그아웃</a><p/>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a href="/admin">회원조회</a>
    </sec:authorize>
</body>
</html>