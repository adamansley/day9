<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Timeline</title>
        <style>
            .flash {color:red;}
        </style>
    </head>
    <body>
        <div class="masthead">
            <img src="images/hubbub_logo.png" width="20%"/>
            <h1>Welcome to Hubbub&trade;, ${user}!</h1>
        </div>
        <div class="nav">
            <a href="main?action=post">Post Something</a>
            |
            <a href="main?action=wall&for=${user}">My Wall</a>
            |
            <a href="main?action=profile&for=${user}">My Profile</a>
            <%--
            |
            <c:choose>
                <c:when test="${not empty target and target ne user and not user.profile.followees.contains(target)}">
                    <a href="main?action=follow&target=${target}"/>Follow ${target}</a> |
                </c:when>
                <c:when test="${not empty target and target ne user and user.profile.followees.contains(target)}">
                    <a href="main?action=unfollow&target=${target}"/>Unfollow ${target}</a> |
                </c:when>
            </c:choose>
            --%>
            <a href="main?action=logout">Log Me Out</a>
        </div>
        <p>Here's What Our Users Have Been Hackin' On:</p>
        <h2 class="flash">${flash}</h2>
        <c:choose>
        <c:when test="${empty posts}">
            <h2>Not a single post? Hubbubuddies&trade;! Get Hubbubabbling&trade;!</h2>
        </c:when>
        <c:otherwise>
        <ul>
            <c:forEach var="post" items="${posts}">
            <li>
                <img src="avatar?for=${post.author}" width="24px"/>
                Posted by
                <a href="main?action=wall&for=${post.author}">${post.author}</a>
                (<f:formatDate type="both" value="${post.posted}"/>)
                <p>${post.content}</p>
            </li>
            </c:forEach>
        </ul>
        </c:otherwise>
        </c:choose>
        <p>
        <c:if test="${pager.page gt 0}">
        <a href="main?action=timeline&page=0">Start</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasNext eq true}">
        <a href="main?action=timeline&page=${pager.next}">Next Page</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasPrev}">
        <a href="main?action=timeline&page=${pager.prev}">Previous Page</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasNext eq true}">
        <a href="main?action=timeline&page=${pager.last}">End</a>
        </c:if>
        </p>
    </body>
</html>
