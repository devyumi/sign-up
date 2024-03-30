<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>방명록</title>

</head>
<body>
<br><br>
<strong>방명록</strong>
<br><br>

    <a href="/home">홈으로</a><br><br>

    <c:forEach items="${guestbooksList}" var="guestbook" varStatus="status">
        ${status.count}<br>
        ${guestbook.nickname}<br>
        ${guestbook.content}<br>
        ${guestbook.createDate}<br><br>
    </c:forEach><br><br>

    <form name="gusetbook" action="/guestbook/write" method="post">
        내용: <textarea cols="60" rows="6"></textarea><br>
             <span style="color: #ff4238">${errorMessage}</span><br>
             <input type="submit" value="등록">
    </form>
</body>
</html>