<%@ page isELIgnored='false'  
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>${s:esc(script.title)} :: Python Web Console</title>
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body class='yui-skin-sam'>
    	<div id='wrapper'>
	      <div id='header'>
	        <div class='busy' style='display:none'>&nbsp;</div>
	        <h1 class='lmenu'>${s:esc(script.title)} :: Python Web Console</h1>
				  <div class='menu'>
				  	<a href='${contextPath}/' class='first'>Home</a> |
				  	<a href='javascript:history.go(-1)'>Back</a> |
				  	<a href='http://blog.thomnichols.org/'>Blog</a>
				  </div>
	      </div>
	      
	      <div id='leftCol'>
		      <div id='scriptInfo'>
		   			by <span class='author'>${s:esc(script.author)}</span>
		   			on <span class='publishDate'>${s:longDate(script.created)}</span>
		      </div>
		      <div id='source'>
			     	<pre class='brush: python'>${s:esc(script.source)}</pre>
		     	</div>

					<div id='comments'>
						<h2>Comments</h2>
						<c:forEach var='comment' items='${comments}'>
						<div class='comment'>
							<div class='hd'>
								<div class='sub'>
									<span class='author'>${s:esc(comment.author)}</span> 
									<span class='date'>${s:shortDate(comment.created)}</span>
								</div>
								<span class='title'>${s:esc(comment.title)}</span>
							</div>
							<div class='bd'>${s:toHTML(comment.text)}</div>
						</div>
						</c:forEach>
					</div>
	     	</div>
	
				<div id='rightCol'>
					<div class='controls'>
						<a href='${contextPath}/console/${script.permalink}' class='first'>Edit</a> |
						<a href='${contextPath}/console/${script.permalink}#run'>Run</a> |
						<a href='#' id='commentLink'>Add Comment</a> |
						<a href='#' id='reportLink'>Report this!</a>
					</div>
		
		     	<div id='tagCloud'>
		     		<h3>Tags</h3>
		     	<c:forEach var='tag' items='${script.tags}'>
		     		<a href='${contextPath}/tag/${tag}'>${s:esc(tag)}</a> &nbsp;
		     	</c:forEach>
		     	</div>
	     	</div>
	
			</div>
			
			<div id='commentDialog' class='hidden'>
				<div class="hd">Add a Comment!</div>
    		<div class="bd">
    			<!-- TODO email for gravatar -->
    			<form id='commentForm' action='${contextPath}/comment/' method='post'>
	      		<div class='row'>
		      		<label for='author'>Your name:</label>
		      		<input type='text' name='author' id='author'></input>
	      		</div>
	      		<div class='row'>
	      			<label for='title'>Subject:</label>
		      		<input type='text' name='title' id='title'></input>
	      		</div>
	      		<div class='row'><label for='content'>Enter your comment below 
	      		  (but please, be nice):</label></div>
	      		<div>
		      		<textarea name='content' id='content' rows='40' cols='40'></textarea>
	      		</div>
            <span id='recaptcha_pub_key' style='display:none'>${recaptchaPublicKey}</span>
            <div id='recaptcha_container'></div>
	      		<input type='hidden' name='script_id' id='script_id' 
	      			value="${script.permalink}"></input>
	      		<div class='hidden'><input type='text' name='junk'></input></div>
	      	</form>
    		</div>
			</div>
			
			<div id='reportDialog' class='hidden'>
 		  	<div class="hd">Report This Script!</div>
    		<div class="bd">
					<p>See something inappropriate here?  Let an administrator know!</p>
					<p>Note that if you'd rather opine on the author's coding skills 
					 (good or bad,) please use the <strong>comment</strong> link instead.</p>
	       	<form id='reportForm' action='${contextPath}/report' method='post'>
	      		<div class='row'>
		      		<label for='fromEmail'>Your contact email (optional):</label>
		      		<input type='text' name='fromEmail' id='fromEmail'></input>
	      		</div>
	      		<div class='row'><label for='content'>Tell us what's wrong:</label></div>
	      		<div>
		      		<textarea name='content' id='content'></textarea>
	      		</div>
	      		<input type='hidden' name='scriptLink' id='scriptLink' 
	      			value="${baseURL}/script/${script.permalink}"></input>
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