<%@ page isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>Python Web Console</title>
        <meta name="google-site-verification" content="p0Hu7aK_PFOKsj_IDZsQTD5nyTpV2vahu4tumL0-Eos" />
        <meta name="y_key" content="a8890ae5456835c2" />
        <meta name="msvalidate.01" content="65623C6210AC5823C328C9EAD6A9DD79" />
        <meta name="description" content="Run and share Python scripts in an online console" />
        <link rel="alternate" type="application/atom+xml" title="Python Web Console RSS Feed" 
              href="${contextPath}/atom.xml" />
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body class="yui-skin-sam">
      <div id='header'>
        <div class='busy' style='display:none'>&nbsp;</div>
        <h1>Python Web Console</h1>
      </div>
      <div>
      <div id='console'>
      	<h2>Source</h2>
        <form id='consoleForm' action='${contextPath}/console/' method='post'>
          <textarea name='src' rows='50' cols='82'><c:choose>
          	<c:when test='${source!=null}'>${s:esc(source)}</c:when>
	          	<c:otherwise># Script text here</c:otherwise>
          </c:choose></textarea>
          <div class='actions'>Actions:
            <input type='submit' id='runBtn' value='run' />
            <input type='submit' id='shareBtn' value='share' />
            <span class='note'>You can execute the script by pressing [Ctrl-Enter]</span>
          </div>
        </form>
      </div>
      <div id='output'>
      	<h2>Output</h2>
        <textarea class='${status}' readonly='readonly' rows='50' cols='82'><c:if test='${output!=null}'>${s:esc(output)}</c:if>
        </textarea>
      </div>
      </div>
      
      <div id='moreItems'>
	      <div id='recentScripts' class='scripts'>
	      	<h3>Recent Items</h3>
	      	<a class='feed' href="${contextPath}/atom.xml" title='Atom Feed'>&nbsp;</a>
	      	<ul>
		      <c:forEach items='${recentScripts}' var='recentScript'>
		      	<li>
		      		<a href='${contextPath}/script/${recentScript.permalink}' 
		      			class='title'>${s:esc(recentScript.title)}</a> 
		      		<span class='author'>${s:esc(recentScript.author)}</span> 
		      		<span class='date'>${s:relativeDate(recentScript.created)}</span>
		      	</li> 
	      	</c:forEach>
	      	</ul>
	      </div>
	      
	      <div id='tagCloud'>
	      	<h3>Popular Tags</h3>
		      <c:forEach items='${tagCloud}' var='tag'>
		      	<span style='font-size:${tag.scale}'><a href='${contextPath}/tag/${tag.name}'><c:out value='${tag.name}'/></a> (${tag.count})</span> &nbsp;
					</c:forEach>
	      </div>

	      <div id='otherLinks'>
	      	<h3>Related Links</h3>
	      	<ul>
		      	<li><a href='http://blog.thomnichols.org/tag/PythonWebConsole'>About this project</a></li>
		      	<li><a href='http://blog.thomnichols.org/'>Thom Nichols</a> (blog)</li>
	      		<li><a href='http://jython.org'>Jython</a></li>
	      		<li><a href='http://github.com/tomstrummer/Python-Web-Console'>Fork this project on GitHub!</a></li>
	      		<li><a href='http://alexgorbatchev.com/wiki/SyntaxHighlighter'>SyntaxHighlighter</a> 
	      			and <a href='http://marijn.haverbeke.nl/codemirror/'>CodeMirror</a> for color!</li>
          </ul>
          <c:if test='${!debug}'>
					<div>
						<a href='http://code.google.com/appengine/'>
		      		<img id="poweredby" alt="Powered by Google App Engine" 
	            src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" /></a>
	        </div>
	        </c:if>
	      </div>      
      </div>
      
      <div id='footer'>&copy; 2010 Thom Nichols
      	<a href='http://validator.w3.org/check?uri=${s:escURL(baseURL)}'>W3C</a>
      	<c:choose>
         	<c:when test='${logoutURL != null}'>
     			<a href='${logoutURL}'>Logout</a>
      		</c:when>
      		<c:otherwise>
      		<a href='${loginURL}'>Login</a>
      		</c:otherwise>
      		</c:choose>
      	
      </div>
      
      <div id='shareDialog'>
 		  	<div class="hd">Share This Script!</div>
    		<div class="bd">
	       	<form id='shareForm' action='${contextPath}/script/' method='post'>
	      		<div class='row'>
		      		<input type='text' name='author' id='author'></input>
		      		<label for='author'>Your name:</label>
	      		</div>
	      		<div class='row'>
		      		<input type='text' name='title' id='title'></input>
		      		<label for='title'>Script Title:</label>
	      		</div>
	      		<div class='row'>
		      		<input type='text' name='tags' id='tags'></input>
		      		<label for='tags'>Tags:</label>
	      		</div>
	      		<input type='hidden' name='source' class='source'></input>
            <span id='recaptcha_pub_key' style='display:none'>${recaptchaPublicKey}</span>
            <div id='recaptcha_container'></div>
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
      <script type="text/javascript" src="${contextPath}/static/js/application.js"></script>
      <script type="text/javascript" src="${contextPath}/static/js/app.console.js"></script>
      </c:when>
      <c:otherwise>
			<script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/yahoo-dom-event/yahoo-dom-event.js&amp;${yui_version}/build/selector/selector-min.js&amp;${yui_version}/build/json/json-min.js&amp;${yui_version}/build/element/element-min.js&amp;${yui_version}/build/connection/connection-min.js&amp;${yui_version}/build/container/container-min.js&amp;${yui_version}/build/button/button-min.js&amp;${yui_version}/build/cookie/cookie-min.js"></script>
      <script type="text/javascript" src="${contextPath}/static/js/ojay/ojay.min.js"></script>
			<script type="text/javascript" src="http://api.recaptcha.net/js/recaptcha_ajax.js"></script>
      <script type="text/javascript" src="${contextPath}/static/js/app.console.min.js"></script>
			</c:otherwise>
      </c:choose>
    </body>
</html>