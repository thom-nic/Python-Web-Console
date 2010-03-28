<%@ page contentType='application/atom+xml; charset=utf-8' isELIgnored='false' 
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@ taglib prefix="s" uri="http://thomnichols.org/jsp/suckless" 
%><?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom">
    <title type="text">Python Web Console</title>
    <subtitle type="text">Comments for Script '${s:esc(script.title)}'</subtitle>
    <updated>${comments[0].createdRFC}</updated>
    <id>${baseURL}/</id>
    <link rel="alternate" type="text/html" hreflang="en" href="${baseURL}" />
    <link rel="self" type="application/atom+xml" href="${baseURL}/atom.xml" />
    <rights>Copyright (c) 2008, Thom Nichols</rights>

    <generator uri="${baseURL}" version="0.1">Python Web Console</generator>
    <c:forEach var='comment' items='${comments}'>
    <entry>
        <title>${s:esc(comment.title)}</title>
        <link rel="alternate" type="text/html" href="${baseURL}/script/${script.permalink}#${comment.permalink}" />
        <id>${baseURL}/script/${scriptID}#${comment.permalink}</id>
        <published>${comment.createdRFC}</published>
				<updated>${comment.createdRFC}</updated>
        <author>
            <name>${s:esc(comment.author)}</name>
            <uri>${baseURL}/user/${s:escURL(comment.author)}</uri>
        </author>
        <content type="xhtml" xml:lang="en" xml:base="${baseURL}">
            <div xmlns="http://www.w3.org/1999/xhtml">
            		<p>${s:esc(comment.text)}</p>
            </div>
        </content>
    </entry>
    </c:forEach>
</feed>