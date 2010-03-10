<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>Python Web Console</title>
        <meta name="google-site-verification" content="" />
        <meta name="y_key" content="" />
        <meta name="msvalidate.01" content="" />
        <link rel="alternate" type="application/rss+xml" title="Python Web Console RSS Feed" 
              href="/atom.xml" />
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body class="yui-skin-sam">
      <div id='header'>
        <div class='busy' style='display:none'>&nbsp;</div>
        <h1>Python Web Console</h1>
      </div>
      <div id='console'>
      	<h2>Source</h2>
        <form id='consoleForm' action='/exec' method='post'>
          <textarea name='src'><c:choose><c:when test='${source!=null}'><c:out value='${source}' escapeXml='true'/></c:when>
	          	<c:otherwise># Script text here</c:otherwise>
          	</c:choose>
          </textarea>
          <div class='actions'>
            <input type='submit' id='runBtn' value='Run' />
            <input type='submit' id='shareBtn' value='Share' />
          </div>
        </form>
      </div>
      <div id='output'>
      	<h2>Output</h2>
        <textarea class='${status}'><c:if test='${output!=null}'><c:out value='${output}' escapeXml='true'/></c:if>
        </textarea>
      </div>
      
      <div id='recentScripts'>
      	<h3>Recent Items</h3>
      <c:forEach items='${recentScripts}' var='recentScript'>
      	<div class='scriptItem'>
      		<a href='/script/${recentScript.permalink}' class='title'><c:out value='${recentScript.title}' /></a> by
      		<span class='author'><c:out value='${recentScript.author}'/></span> on
      		<span class='date'><c:out value='${recentScript.created}'/></span> 
      	</div>
      </c:forEach>
      </div>
      
      <div id='tags'>
      	<h3>Popular Tags</h3>
	      <c:forEach items='${tagCloud}' var='tag'>
	      	<span><a href='/tag/${tag.name}'><c:out value='${tag.name}'/></a> (${tag.count})</span> &nbsp;
				</c:forEach>
      </div>
      
      <div id='otherLinks'>
      	<h3>Related Links</h3>
      	<a href='http://blog.thomnichols.org'>Thom Nichols</a>
      	<a href='http://code.google.com/appengine/'>AppEngine</a>
      	<a href='http://jython.org'>Jython</a>
      </div>
      
      <div id='footer'>&copy; 2010 Thom Nichols</div>
      
      <div id='shareDialog'>
 		  	<div class="hd">Share This Script!</div>
    		<div class="bd">
	       	<form id='shareForm' action='script' method='post'>
	      		<div>
		      		<label for='author'>Your name:</label>
		      		<input type='text' name='author' id='author'></input>
	      		</div>
	      		<div>
		      		<label for='author'>Script Title:</label>
		      		<input type='text' name='title' id='title'></input>
	      		</div>
	      		<div>
		      		<label for='author'>Tags:</label>
		      		<input type='text' name='tags' id='tags'></input>
	      		</div>
	      		<input type='hidden' name='source' class='source'></input>
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
      <script type="text/javascript" src="/static/js/ojay/js-class.js"></script>
      <script type="text/javascript" src="/static/js/ojay/core.js"></script>
			<script type="text/javascript" src="/static/js/ojay/pkg/forms.js"></script>
      </c:when>
      <c:otherwise>
      <script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/yahoo-dom-event/yahoo-dom-event.js&${yui_version}/build/selector/selector-min.js"></script>
			<script type="text/javascript" src="http://yui.yahooapis.com/combo?${yui_version}/build/json/json-min.js&${yui_version}/build/element/element-min.js&${yui_version}/build/connection/connection-min.js&${yui_version}/build/container/container-min.js&${yui_version}/build/button/button-min.js"></script>
      <script type="text/javascript" src="/static/js/ojay/js-class-min.js"></script>
      <script type="text/javascript" src="/static/js/ojay/core-min.js"></script>
			<script type="text/javascript" src="/static/js/ojay/pkg/forms-min.js"></script>
			</c:otherwise>
      </c:choose>
      <script type="text/javascript" src="/static/js/application.js"></script>
      
      <img id="poweredby" alt="Powered by Google App Engine" 
                      src="http://code.google.com/appengine/images/appengine-noborder-120x30.gif" />
    </body>
</html>