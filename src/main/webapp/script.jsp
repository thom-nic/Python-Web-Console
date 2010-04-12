<%@ page isELIgnored='false'  
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
      <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
      <title>${s:esc(script.title)} :: Python Web Console</title>
      <jsp:include page='/WEB-INF/head.jsp' />
			<c:choose>
			<c:when test='${debug}'>
			<link href='${contextPath}/static/hosted/syntax/shCore.css' rel="stylesheet" type="text/css" />
			<link href='${contextPath}/static/hosted/syntax/shThemeFadeToGrey.css' rel="stylesheet" type="text/css" />
			</c:when>
			<c:otherwise>
			<link href='${contextPath}/static/hosted/syntax/sh.min.css' rel="stylesheet" type="text/css" />
			</c:otherwise>
			</c:choose>
    </head>
    <body class='yui-skin-sam'>
    	<div id='wrapper'>
	      <div id='header'>
	        <div class='busy' style='display:none'>&nbsp;</div>
	        <h1 class='lmenu'>${s:esc(script.title)} :: Python Web Console</h1>
				  <div class='menu'>
				  	<a href='${contextPath}/' class='first'>Home</a> |
				  	<a href='javascript:history.go(-1)'>Back</a> |
				  	<a href='http://blog.thomnichols.org/tag/PythonWebConsole'>Blog</a>
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
						<a class='feed' title='Atom Feed for comments on this script'
							href="${contextPath}/script/atom.xml?script=${s:escURL(script.permalink)}">&nbsp;</a>
						<h2>Comments</h2>
						<c:forEach var='comment' items='${comments}'>
						<div class='comment'>
							<c:if test='${comment.emailHash!=null}'>
							<img class="gravatar" alt="avatar" 
								src="http://www.gravatar.com/avatar/${comment.emailHash}?s=32&d=identicon" />
							</c:if>
							<div class='hd'>
								<div class='sub'>
									<a name='${comment.permalink}'></a>
									<span class='author'>${s:esc(comment.author)}</span> 
									<span class='date'>${s:shortDate(comment.created)}</span>
								</div>
								<span class='title'>${s:esc(comment.title)}</span>
							</div>
							<div class='bd'>${s:toHTML(s:esc(comment.text))}</div>
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
		     	
		     	<div id='share'>
		     		<div class='admin'><
		     			c:if test='${admin}'>
							<a href='#' id='deleteLink' title='Delete this post'>Delete</a></c:if>
						</div><
		     		c:set var='fullURL' value='${baseURL}/script/${script.permalink}' scope='request'/><
		     		c:set var='tweet' value='Python Web Console: ${script.title} ${fullURL}' scope='request'/>
		     		<a href='http://twitter.com/home?source=pythonwebconsole.thomnichols.org&status=${s:escURL(tweet)}'
		     				title='Share on Twitter' target="_new">
		     			<img src='${contextPath}/static/img/tw_share_16.png' alt='Share on Twitter'/></a>
		     		<a href='http://www.google.com/reader/link?srcURL=http://pythonwebconsole.thomnichols.org&url=${s:escURL(fullURL)}&title=Python%20Web%20Console:%20${s:escURL(script.title)}' 
		     				title='Share on Google Buzz' target="_new">
		     			<img src='${contextPath}/static/img/buzz_share_16.png' alt='Share on Google Buzz'/></a>
		     		<a href='http://www.facebook.com/sharer.php?u=${s:escURL(fullURL)}&t=Python%20Web%20Console:%20${s:escURL(script.title)}'
		     				title='Share on Facebook' target="_new">
		     			<img src='${contextPath}/static/img/fb_share_16.png' alt='Share on Facebook'/></a>
		     	</div>
	     	</div>
	
	      <div id='footer'>&copy; 2010 Thom Nichols</div>
			</div>
			
			<div id='commentDialog' class='hidden'>
				<div class="hd">Add a Comment!</div>
    		<div class="bd">
    			<form id='commentForm' action='${contextPath}/comment/' method='post'>
	      		<div class='row'>
		      		<label for='author'>Your name:</label>
		      		<input type='text' name='author' id='author'></input>
	      		</div>
	      		<div class='row'>
		      		<label for='email'>Email:</label>
		      		<span class='note'>(optional, for Gravatar)</span>
		      		<input type='text' name='email' id='email'></input>
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
	      		<div class='row'><label for='issue'>Tell us what's wrong:</label></div>
	      		<div>
		      		<textarea name='issue' id='issue' rows='40' cols='40'></textarea>
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
			<script type="text/javascript" src="/static/hosted/yui/json/json.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/element/element.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/connection/connection.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/container/container.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/button/button.js"></script>
			<script type="text/javascript" src="/static/hosted/yui/cookie/cookie.js"></script>
      <script type="text/javascript" src="/static/js/ojay/js-class.js"></script>
      <script type="text/javascript" src="/static/js/ojay/core.js"></script>
			<script type="text/javascript" src="/static/js/ojay/pkg/forms.js"></script>
      <script src='${contextPath}/static/hosted/syntax/shCore.js' type='text/javascript'></script>
			<script src='${contextPath}/static/hosted/syntax/shBrushPython.js' type='text/javascript'></script> 
      <script type="text/javascript" src="${contextPath}/static/js/application.js"></script>
			<script type="text/javascript" src="${contextPath}/static/js/app.script.js"></script>
			</c:when>
			<c:otherwise>
			<script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/yahoo-dom-event/yahoo-dom-event.js&amp;${yui_version}/build/selector/selector-min.js&amp;${yui_version}/build/json/json-min.js&amp;${yui_version}/build/element/element-min.js&amp;${yui_version}/build/connection/connection-min.js&amp;${yui_version}/build/container/container-min.js&amp;${yui_version}/build/button/button-min.js&amp;${yui_version}/build/cookie/cookie-min.js"></script>
      <script type="text/javascript" src="${contextPath}/static/js/ojay/ojay.min.js"></script>
			<script type="text/javascript" src="http://api.recaptcha.net/js/recaptcha_ajax.js"></script>
      <script src='${contextPath}/static/hosted/syntax/sh.min.js' type='text/javascript'></script>
      <script type="text/javascript" src="${contextPath}/static/js/app.script.min.js"></script>
			</c:otherwise>
			</c:choose>
    </body>
</html>