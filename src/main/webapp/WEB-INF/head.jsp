<%@ page isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%>        <link rel="shortcut icon" href="${contextPath}/static/img/favicon.ico" type="image/x-icon" />
        <link rel="apple-touch-icon" href="/static/images/apple-touch-icon.png" />
        <c:choose>
        <c:when test='${debug}'>
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/base-min.css" />
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/assets/skins/sam/skin.css" />
        <link rel="stylesheet" href="/static/style.css" type="text/css" media="screen,print" />
        </c:when>
        <c:otherwise>
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/reset-fonts-grids/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/base/base-min.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/assets/skins/sam/skin.css" />
        <link rel="stylesheet" href="/static/style.min.css" type="text/css" media="screen,print" />
        </c:otherwise>
        </c:choose>
