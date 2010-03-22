<%@ page contentType='text/html' isELIgnored='false' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9
      http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
<url>
	<loc>${baseURL}/</loc>
	<priority>1.00</priority>
	<changefreq>daily</changefreq>
</url>
<c:forEach var='script' items='${scripts}'>
<url>
	<loc>${baseURL}/${script.permalink}</loc>
	<priority>0.80</priority>
	<lastmod>${script.createdRFC}</lastmod>
	<changefreq>monthly</changefreq>
</url>
</c:forEach>
</urlset>