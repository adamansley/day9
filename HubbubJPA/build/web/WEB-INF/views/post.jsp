<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hubbub&trade; &raquo; Post</title>
        <style>.flash {color:red;}</style>
    </head>
    <body>
        <div class="masthead">
            <img src="images/hubbub_logo.png" width="20%"/>
            <h1>What Are You Hackin' on Today?</h1>
        </div>
        <div class="nav">
            <a href="main?action=timeline">Back to the Timeline</a> |
            <a href="main?action=wall">Go to My Wall</a> |
            <a href="main?action=profile&for=${user}">Check My Profile</a> |
            <a href="main?action=logout">Log Me Out</a>
        </div>
        <c:if test="${not empty flash}">
            <h2 class="flash">${flash}</h2>
        </c:if>
        <img src="avatar?for=${user}" width="48px"/>
        <form method="POST" action="main">
            <input type="hidden" name="action" value="post"/>
            <table>
                <tr>
                    <td>
            <textarea cols="50" rows="6" name="content">${content}</textarea>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><input type="submit" value="Tell the World"/></td>
                </tr>
            </table>
        </form>
        <c:if test="${not empty lastPost}">
        <ul>
            <li>
                <img src="avatar?for=${user}" width="24px"/>
                <span class="postdate">
                    <f:formatDate type="both" value="${lastPost.posted}"/>
                </span>
                <p class="content">${lastPost.content}</p>
            </li>
        </ul>
        </c:if>
    </body>
</html>
