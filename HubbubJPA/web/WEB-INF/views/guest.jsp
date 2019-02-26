<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Guest</title>
        <style>
            .flash {color:red;}
        </style>
    </head>
    <body>
        <div class="masthead">
            <img src="images/hubbub_logo.png" width="20%"/>
            <h1>Welcome to Hubbub&trade;!</h1>
        </div>
        <div class="nav">
            <a href="main?action=login">I'm a Member</a>
            |
            <a href="main?action=join">Sign Me Up!</a>
        </div>
        <p>Here's what our users have been hackin' on:</p>
        <h2 class="flash">${flash}</h2>
        <ul>
            <c:forEach var="post" items="${posts}">
                <li>
                    Posted by ${post.author}
                    (<f:formatDate type="both" value="${post.posted}"/>)
                    <p>${post.content}</p>
                </li>
            </c:forEach>
        </ul>
    </body>
</html>
