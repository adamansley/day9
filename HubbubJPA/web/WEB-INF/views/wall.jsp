<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Wall &raquo; ${target}</title>
        <style>
            .flash {color:red;}
        </style>
    </head>
    <body>
        <div class="masthead">
            <img src="images/hubbub_logo.png" width="20%"/>
            <h1>Hubbub&trade; Wall for ${target}!</h1>
        </div>
        <div class="nav">
            <a href="main?action=timeline">Back to the Timeline</a>
            |
            <a href="main?action=post">Post Something</a>
            |
            <c:if test="${target ne user}">
            <a href="main?action=wall&for=${user}">My Wall</a>
            |
            </c:if>
            <a href="main?action=profile&for=${user}">My Profile</a>
            |
            <c:choose>
                <c:when test="${user.followees.contains(target)}">
                    <a href="main?action=unfollow&target=${target}"/>Unfollow ${target}</a> |
                </c:when>
                <c:otherwise>
                    <a href="main?action=follow&target=${target}"/>Follow ${target}</a> |
                </c:otherwise>
            </c:choose>
            <a href="main?action=logout">Log Me Out</a>
        </div>
        <%--
            If there's no target (username), this is the timeline
            If the target is the user, this is our own wall, so include followee posts
            If the target is not the user, this is their wall, only include their posts
        --%>
        <c:choose>
            <c:when test="${target ne user}">
        <p>Here's What's Hubbubbling&trade; with ${target}</p>               
            </c:when>
            <c:otherwise>
        <p>Here's what's Hubbubabbling&trade; around you and your Hubbubuddies&trade;</p>
            </c:otherwise>
        </c:choose>

        <h2 class="flash">${flash}</h2>
        <c:choose>
            <c:when test="${empty posts}">
        <h2>No posts to show? Get Hubbubabbling&trade;!</h2>
            </c:when>
        <c:otherwise>
        <ul>
            <c:forEach var="post" items="${posts}">
            <li>
                <img src="avatar?for=${post.author}" width="24px"/>
                Posted by
                <a href="main?action=profile&for=${post.author}">${post.author}</a>
                (<f:formatDate type="both" value="${post.posted}"/>)
                <p>${post.content}</p>
            </li>
            </c:forEach>
        </ul>
        </c:otherwise>
        </c:choose>
        <p>
        <c:if test="${pager.page gt 0}">
        <a href="main?action=wall&for=${target}&page=0">Start</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasNext eq true}">
        <a href="main?action=wall&for=${target}&page=${pager.next}">Next Page</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasPrev}">
        <a href="main?action=wall&for=${target}&page=${pager.prev}">Previous Page</a>&nbsp;&nbsp;&nbsp;
        </c:if>
        <c:if test="${pager.hasNext eq true}">
        <a href="main?action=wall&for=${target}&page=${pager.last}">End</a>
        </c:if>
        </p>
    </body>
</html>
