<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>${title}</title>
        <meta name="google-site-verification" content="" />
        <meta name="y_key" content="" />
        <meta name="msvalidate.01" content="" />
        <link rel="alternate" type="application/rss+xml" title="Python Web Console RSS Feed" 
              href="/atom.xml" />
        <link rel="apple-touch-icon" href="/static/images/apple-touch-icon.png"/>
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/reset-fonts-grids/reset-fonts-grids.css" />
        <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.0r4/build/base/base-min.css" />
        <link rel="stylesheet" href="/static/style.css" type="text/css" media="screen,print" />
    </head>
    <body>
      <div id='header'>
        <h1>Python Web Console</h1>
      </div>
      <div id='console'>
        <form id='consoleForm' action='/' method='post'>
          <textarea name='src'><c:choose><c:when test='${source!=null}'><c:out value='${source}' escapeXml='true'/></c:when>
	          	<c:otherwise># Script text here</c:otherwise>
          	</c:choose>
          </textarea>
          <div class='actions'>
            <input type='submit' id='run' value='Run' />
            <input type='submit' id='share' value='Share' />
          </div>
        </form>
      </div>
      <div id='output'>
        <textarea class='${status}'><c:if test='${output!=null}'><c:out value='${output}' escapeXml='true'/></c:if>
        </textarea>
      </div>
      
      <div id='recentScripts'>
      </div>
      
      <div id='otherLinks'>
      </div>
      
      <div id='footer'>&copy; 2010 Thom Nichols
      </div>
    </body>
</html>