package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Script;

public class SitemapServlet extends HttpServlet {
	private static final long serialVersionUID = 8238488369957913605L;
	final Logger log = LoggerFactory.getLogger( getClass() );	
	private PersistenceManagerFactory pmf; 
	
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Query q = pm.newQuery( Script.class );
			q.setOrdering( "created desc" );
			q.setRange( 0,30 );
			List<Script> scripts = (List<Script>)q.execute();
			
			req.setAttribute( "scripts", scripts );
			req.getRequestDispatcher( "/WEB-INF/sitemap.xml.jsp" ).forward( req, resp );
		}
		finally { pm.close(); }
	}
	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
	}
}
