<%@ page isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
    <title>Error :: Python Web Console</title>
    <jsp:include page='/WEB-INF/head.jsp' />
    <style type="text/css">
#errCode, #errMsg, #links { text-align: center }
#errMsg {
  background-color: #fff; 
  position: relative; 
  top: 135px;
  z-index: 1;
}
#errCode {
  font-size:300px;
  font-weight:bold;
  line-height:258px;
}
#footer { 
  border-top:2px solid #999;
}
    </style>
  </head>
  <body>
  	<div id='wrapper'>
	    <div id='header'>
		    <h1 class='lmenu'>Python Web Console</h1>
			  <div>
			  	<a href='${contextPath}/'>Home</a> &nbsp;&nbsp;|&nbsp;&nbsp;
			  	<a href='javascript:history.go(-1)'>Back</a> &nbsp;&nbsp;|&nbsp;&nbsp;
			  	<a href='http://blog.thomnichols.org/'>Blog</a>
			  </div>
		  </div>
			  <div id='errMsg'>OMG something blew up.</div>
			  <div id='errCode'>500</div>
			  <div id='footer'></div>
		  </div>
		</div>
  </body>
</html>
       