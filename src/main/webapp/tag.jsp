<%@ page isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
    <title>Recent scripts tagged with '${s:esc(tag.name)}' :: Python Web Console</title>
    <link rel="alternate" type="application/atom+xml" 
    	title="Python Web Console RSS Feed for tag '${s:esc(tag.name)}'" 
      href="${contextPath}/atom.xml?tag=${s:escURL(tag.name)}" />
    <jsp:include page='/WEB-INF/head.jsp' />
    <meta name='description' content="Scripts tagged with '${s:esc(tag.name)}'" />
  </head>
  <body>
  	<div id='wrapper'>
	    <div id='header'>
	      <h1>Scripts tagged with '${s:esc(tag.name)}' :: Python Web Console</h1>
	    </div>

			<div id='leftCol'>	    
		    <p>There are ${tag.count} scripts tagged with '${s:esc(tag.name)}.' 
		    	Here are the ${s:size(scripts)} most recent.</p>
		     
		    <ul>
		    <c:forEach var='script' items='${scripts}'>
		    	<li>
		    		<a href='${contextPath}/script/${script.permalink}'>${s:esc(script.title)}</a>
		    		by <span class='author'>${s:esc(script.author)}</span>
		 				on <span class='publishDate'>${s:longDate(script.created)}</span>
		 			</li>
		   	</c:forEach>
		    </ul>
	    </div>
	    <div id='rightCol'>
	    	<a class='feed' title='Subscribe to scripts matching this tag'
					href="${contextPath}/atom.xml?tag=${s:escURL(tag.name)}" >&nbsp;</a>	      
	    </div>
    </div>
  </body>
</html>