<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>${title}</title>
        <link rel="alternate" type="application/rss+xml" title="Python Web Console RSS Feed" 
              href="/atom.xml" />
        <link rel="apple-touch-icon" href="/static/images/apple-touch-icon.png"/>
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/reset-fonts-grids/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/base/base-min.css" />
        <link rel="stylesheet" href="/static/style.css" type="text/css" media="screen,print" />
    </head>
    <body>
      <div id='header'>
        <h1>${script.title} :: Python Web Console</h1>
      </div>
      
      <div id='scriptInfo'>
   			by <span class='author'><c:out value='${script.author}' /></span>
   			on <span class='publishDate'><c:out value='${script.created}' /></span>
      </div>
     	<pre id='source' class='brush: python'><c:out value='${script.source}' escapeXml='true'/></pre>
     	<div id='tags'>
     	<c:forEach var='tag' items='${script.tags}'>
     		<a href='/tag/${tag}'>${tag}</a> &nbsp;
     	</c:forEach>
     	</div>

			<div id='controls'>
				<a href='/console/${script.permalink}'>Edit</a>
				<a href='/console/post/${script.permalink}'>Run</a>
				<a href='#'>Add Comment</a>
			</div>

			<div id='comments'>(comments go here)</div>
			
			<c:choose>
			<c:when test='${debug}'>
			<link href='/static/hosted/syntax/shCore.css' rel="stylesheet" type="text/css" />
			<link href='/static/hosted/syntax/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
      <script src='/static/hosted/syntax/shCore.js' type='text/javascript'></script>
			<script src='/static/hosted/syntax/shBrushPython.js' type='text/javascript'></script> 
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