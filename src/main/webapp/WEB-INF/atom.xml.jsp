<%@ page contentType='application/atom+xml; charset=utf-8' isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom">
    <title type="text">Python Web Console</title>
    <subtitle type="text">${subTitle}</subtitle>
    <updated>${scripts[0].createdRFC}</updated>
    <id>${baseURL}/</id>
    <link rel="alternate" type="text/html" hreflang="en" href="${baseURL}" />
    <link rel="self" type="application/atom+xml" href="${baseURL}/atom.xml" />
    <rights>Copyright (c) 2008, Thom Nichols</rights>

    <generator uri="${baseURL}" version="0.1">Python Web Console</generator>
    <c:forEach var='script' items='${scripts}'>
    <entry>
        <title>${s:esc(script.title)}</title>
        <link rel="alternate" type="text/html" href="${baseURL}/script/${script.permalink}" />
        <id>${baseURL}/script/${script.permalink}</id>
        <published>${script.createdRFC}</published>
				<updated>${script.createdRFC}</updated>
        <author>
            <name>${s:esc(script.author)}</name>
            <uri>${baseURL}/user/${s:escURL(script.author)}</uri>
        </author>
        <content type="xhtml" xml:lang="en" xml:base="${baseURL}">
            <div xmlns="http://www.w3.org/1999/xhtml">
            		<pre>${s:esc(script.source)}</pre>
           			<h4>Tags:</h4>
            		<p><c:forEach var='tag' items='${script.tags}'>
     							|&#160;&#160; <a href='${baseURL}/tag/${s:escURL(tag)}'>${s:esc(tag)}</a> &#160;&#160; 
     						</c:forEach></p>
                <p><a href="${baseURL}/script/${script.permalink}#comments">Comments &#8230;</a></p>
            </div>
        </content>
        <c:forEach var='tag' items="${script.tags}">
        <category term="${s:esc(tag)}" />
        </c:forEach>
    </entry></c:forEach>
</feed>