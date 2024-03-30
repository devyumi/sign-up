<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time"%>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE html>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>방명록</title>

    <script type="text/javascript">
        function checkContent(){
            if(document.guestbook.content.value.length == 0){
                alert("내용을 입력해주세요.");
            } else if(document.guestbook.content.value.length < 5){
                alert("5자 이상 입력해주세요.");
            }
        }
    </script>

</head>
<body>
<br><br>
<strong>방명록</strong>
<br><br>

    <a href="/home">홈으로</a><br><br>

    <c:forEach items="${guestbooksList}" var="guestbook" varStatus="status">
        <div><${status.count}></div><br>
        <div style="font-style: italic">${guestbook.nickname}</div>
        <div>${guestbook.content}</div>
        <div><javatime:format value="${guestbook.createDate}" pattern="yyyy-MM-dd HH:mm"/></div>
        <div>- - - - - - - - - - - - - - - - - - - - - - - - - - - -</div><br>
    </c:forEach><br><br>

    <form name="guestbook" action="/guestbook/write" method="post" onsubmit="checkContent()">
        <div>
            <textarea name="content" cols="50" rows="5" style="resize: none" placeholder="내용을 입력해주세요."></textarea>
        </div>
        <div>
            <input type="submit" value="등록">
        </div>
    </form>
</body>
</html>