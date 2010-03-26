package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitemapPingServlet extends HttpServlet {
	private final Logger log = LoggerFactory.getLogger( getClass() );
	private String sitemapURL;
	
	private final Map<String, String> engines = new HashMap<String,String>();

	@Override
	public void init() throws ServletException {
		engines.put("google","http://www.google.com/webmasters/tools/ping?sitemap=");
		engines.put("bing","http://www.bing.com/webmaster/ping.aspx?siteMap=");
		try {
			String baseURL = (String)getServletContext().getAttribute( "baseURL" );
			this.sitemapURL = URLEncoder.encode( baseURL + "/sitemap.xml", "utf-8" );
		}
		catch ( UnsupportedEncodingException ex ) {
			throw new ServletException( "Error in servlet init", ex );
		}
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String engine = req.getParameter( "engine" );
		if ( engine == null || "".equals(engine.trim()) ) {
			resp.sendError( 400, "WTF" );
			return;
		}
		HttpURLConnection conn = (HttpURLConnection) new URL( 
				engines.get( engine ) + sitemapURL ).openConnection();
		try {
			int code = conn.getResponseCode();
			if ( code < 200 || code > 299 )
				log.warn( "Unexpected response ({}) for sitemap ping to '{}'", code, engine );
			else log.debug( "Sitemap ping to {}: {}", engine, conn.getResponseMessage() );
		}
		finally { try { conn.getInputStream().close(); } catch ( Exception ex ) {} }
	}
}
