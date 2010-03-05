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
        <link rel="apple-touch-icon" href="/static/images/apple-touch-icon.png"/>
        <c:choose>
        <c:when test='${debug}'>
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/reset-fonts-grids/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/base-min.css" />
        <link rel="stylesheet" type="text/css" href="/static/hosted/yui/assets/skins/sam/skin.css" />
        </c:when>
        <c:otherwise>
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/reset-fonts-grids/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/base/base-min.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/${yui_version}/build/assets/skins/sam/skin.css" />
        </c:otherwise>
        </c:choose>
        <link rel="stylesheet" href="/static/style.css" type="text/css" media="screen,print" />
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
      </div>
      
      <div id='otherLinks'>
      </div>
      
      <div id='footer'>&copy; 2010 Thom Nichols
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