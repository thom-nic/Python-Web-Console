<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title><c:out value='${script.title}' /> :: Python Web Console</title>
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body class='yui-skin-sam'>
    	<div id='wrapper'>
	      <div id='header'>
	        <h1><c:out value='${script.title}' /> :: Python Web Console</h1>
	      </div>
	      
	      <div id='leftCol'>
		      <div id='scriptInfo'>
		   			by <span class='author'><c:out value='${script.author}' /></span>
		   			on <span class='publishDate'><c:out value='${script.created}' /></span>
		      </div>
		      <div id='source'>
			     	<pre class='brush: python'><c:out value='${script.source}' escapeXml='true'/></pre>
		     	</div>

					<div id='comments'>
						<h3>Comments</h3>
						(comments go here)
					</div>
	     	</div>
	
				<div id='rightCol'>
					<div class='controls'>
						<a href='${contextPath}/console/${script.permalink}'>Edit</a>
						<a href='${contextPath}/console/${script.permalink}#run'>Run</a>
						<a href='#'>Add Comment</a>
						<a href='#' id='reportLink'>Report this!</a>
					</div>
		
		     	<div id='tagCloud'>
		     		<h3>Tags</h3>
		     	<c:forEach var='tag' items='${script.tags}'>
		     		<a href='${contextPath}/tag/${tag}'><c:out value='${tag}' /></a> &nbsp;
		     	</c:forEach>
		     	</div>
	     	</div>
	
			</div>
			
			<div id='commentDialog' style='display:none'>
			</div>
			
			<div id='reportDialog'>
 		  	<div class="hd">Report This Script!</div>
    		<div class="bd">
					<p>See something inappropriate here?  Let an administrator know!</p>
					<p>Note that if you'd rather opine on the author's coding skills, 
					 good or bad, use the <strong>comment</strong> link instead.</p>
	       	<form id='reportForm' action='${contextPath}/report' method='post'>
	      		<div>
		      		<label for='fromEmail'>Your contact email (optional):</label>
		      		<input type='text' name='fromEmail' id='fromEmail'></input>
	      		</div>
	      		<div><label for='content'>Tell us what's wrong:</label></div>
	      		<div>
		      		<textarea name='content' id='content'></textarea>
	      		</div>
	      		<input type='hidden' name='scriptLink' id='scriptLink' 
	      			value="${baseURL}/console/${script.permalink}"></input>
	      		<div class='hidden'><input type='text' name='junk'></input></div>
	      	</form>
      	</div>
			</div>
			
			<c:choose>
			<c:when test='${debug}'>
      <script type="text/javascript" src='/static/hosted/yui/yahoo-dom-event/yahoo-dom-event.js'></script>
      <script type="text/javascript" src='/static/hosted/yui/selector/selector.js'></script>
			<script type="text/javascript" src="/static/hosted/yui/element/element.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/connection/connection.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/container/container.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/button/button.js"></script>
      <script type="text/javascript" src="/static/js/ojay/js-class.js"></script>
      <script type="text/javascript" src="/static/js/ojay/core.js"></script>
			<script type="text/javascript" src="/static/js/ojay/pkg/forms.js"></script>
			<link href='${contextPath}/static/hosted/syntax/shCore.css' rel="stylesheet" type="text/css" />
			<link href='${contextPath}/static/hosted/syntax/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
      <script src='${contextPath}/static/hosted/syntax/shCore.js' type='text/javascript'></script>
			<script src='${contextPath}/static/hosted/syntax/shBrushPython.js' type='text/javascript'></script> 
			</c:when>
			<c:otherwise>
      <script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/yahoo-dom-event/yahoo-dom-event.js&${yui_version}/build/selector/selector-min.js"></script>
			<script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/json/json-min.js&${yui_version}/build/element/element-min.js&${yui_version}/build/connection/connection-min.js&${yui_version}/build/container/container-min.js&${yui_version}/build/button/button-min.js"></script>
      <script type="text/javascript" src="/static/js/ojay/js-class-min.js"></script>
      <script type="text/javascript" src="/static/js/ojay/core-min.js"></script>
			<script type="text/javascript" src="/static/js/ojay/pkg/forms-min.js"></script>
			<link href='http://alexgorbatchev.com/pub/sh/current/styles/shCore.css' rel="stylesheet" type="text/css" />
			<link href='http://alexgorbatchev.com/pub/sh/current/styles/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
      <script src='http://alexgorbatchev.com/pub/sh/current/scripts/shCore.js' type='text/javascript'></script>
			<script src='http://alexgorbatchev.com/pub/sh/current/scripts/shBrushPython.js' type='text/javascript'></script>
			</c:otherwise>
			</c:choose>
      <script type="text/javascript" src="${contextPath}/static/js/application.js"></script>
			<script type="text/javascript" src="${contextPath}/static/js/app.script.js"></script>
			<script language="javascript">
			  //SyntaxHighlighter.defaults['light'] = true;
			  SyntaxHighlighter.defaults['wrap-lines'] = false;
			  SyntaxHighlighter.all();
			</script>      
    </body>
</html>