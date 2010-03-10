<%@ page isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
        <title>Recent scripts tagged with '<c:out value='${tag.name}'/>' :: Python Web Console</title>
        <jsp:include page='/WEB-INF/head.jsp' />
    </head>
    <body>
      <div id='header'>
        <h1>Recent scripts tagged with '<c:out value='${tag.name}'/>'</h1>
      </div>
      
      <p>There are ${tag.count} scripts tagged with '<c:out value='${tag.name}'/>.'  
      	Here are the 20 most recent.</p>
      
      <ul>
      <c:forEach var='script' items='${scripts}'>
      	<li>
      		<a href='/script/${script.permalink}'><c:out value='${script.title}' /></a>
      		by <span class='author'><c:out value='${script.author}' /></span>
   				on <span class='publishDate'><c:out value='${script.created}' /></span>
   			</li>
     	</c:forEach>
      </ul>
    </body>
</html>