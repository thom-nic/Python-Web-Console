<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title><c:out value='${script.title}' /> :: Python Web Console</title>
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body>
      <div id='header'>
        <h1><c:out value='${script.title}' /> :: Python Web Console</h1>
      </div>
      
      <div id='scriptInfo'>
   			by <span class='author'><c:out value='${script.author}' /></span>
   			on <span class='publishDate'><c:out value='${script.created}' /></span>
      </div>
     	<pre id='source' class='brush: python'><c:out value='${script.source}' escapeXml='true'/></pre>
     	<div id='tags'>
     		<h3>Tags</h3>
     	<c:forEach var='tag' items='${script.tags}'>
     		<a href='${contextPath}/tag/${tag}'><c:out value='${tag}' /></a> &nbsp;
     	</c:forEach>
     	</div>

			<div id='controls'>
				<a href='/console/${script.permalink}'>Edit</a>
				<a href='/console/${script.permalink}#Run'>Run</a>
				<a href='#'>Add Comment</a>
			</div>

			<div id='comments'>
				<h3>Comments</h3>
				(comments go here)
			</div>
			
			<c:choose>
			<c:when test='${debug}'>
			<link href='${contextPath}/static/hosted/syntax/shCore.css' rel="stylesheet" type="text/css" />
			<link href='${contextPath}/static/hosted/syntax/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
      <script src='${contextPath}/static/hosted/syntax/shCore.js' type='text/javascript'></script>
			<script src='${contextPath}/static/hosted/syntax/shBrushPython.js' type='text/javascript'></script> 
			</c:when>
			<c:otherwise>
			<link href='http://alexgorbatchev.com/pub/sh/current/styles/shCore.css' rel="stylesheet" type="text/css" />
			<link href='http://alexgorbatchev.com/pub/sh/current/styles/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
      <script src='http://alexgorbatchev.com/pub/sh/current/scripts/shCore.js' type='text/javascript'></script>
			<script src='http://alexgorbatchev.com/pub/sh/current/scripts/shBrushPython.js' type='text/javascript'></script>
			</c:otherwise>
			</c:choose>
			<script language="javascript">
			  //SyntaxHighlighter.defaults['light'] = true;
			  SyntaxHighlighter.defaults['wrap-lines'] = false;
			  SyntaxHighlighter.all();
			</script>      
    </body>
</html>