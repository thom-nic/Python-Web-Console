/**
 * 
 */
package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
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
import org.thomnichols.pythonwebconsole.model.Tag;

/**
 * @author tnichols
 *
 */
public class RootServlet extends HttpServlet {
	private static final long serialVersionUID = 6074475136160037320L;
	final Logger log = LoggerFactory.getLogger( getClass() );
	private PersistenceManagerFactory pmf;
	
	@Override
	public void init() throws ServletException {
		Enumeration<String> params = super.getInitParameterNames();
		while ( params.hasMoreElements() ) {
			String param = params.nextElement();
			String value = super.getInitParameter( param );
			if ( "true".equalsIgnoreCase( value ) )
				super.getServletContext().setAttribute( param, true );
			else if ( "false".equalsIgnoreCase( value ) )
				super.getServletContext().setAttribute( param, false );
			else super.getServletContext().setAttribute( param, value );
		}
		super.getServletContext().setAttribute( "baseURL",
				super.getInitParameter( "baseURL" ) + 
				getServletContext().getContextPath() );

		this.pmf = JDOHelper.getPersistenceManagerFactory("datastore");
		super.getServletContext().setAttribute( "persistence", pmf );
	}

	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		PersistenceManager pm = pmf.getPersistenceManager();
        try {
        	req.setAttribute("recentScripts", getRecentScripts(pm));
        	req.setAttribute("tagCloud", getTagCloud(pm));
			super.getServletContext().
				getRequestDispatcher( "/index.jsp" ).forward( req, resp );
        } finally { pm.close(); }
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String result = "(no output)";
		String source = req.getParameter( "src" ); 
		String status = "normal";
		try {
			if ( source != null ) result = new ConsoleService().exec( source );
		}
		catch( Exception ex ) {
			log.warn( "Script execution error", ex );
			result = ex.getMessage();
			status = "error";
		}
		req.setAttribute( "source", source );
		req.setAttribute( "output", result );
		req.setAttribute( "status", status );
		super.getServletContext().
			getRequestDispatcher( "/index.jsp" ).forward( req, resp );
	}
	
	private Collection<Tag> getTagCloud( PersistenceManager pm ) {
		Query q = pm.newQuery( Tag.class, "count > 0" );
		q.setOrdering("count desc");
		q.setRange(0,15);
		// reorder tags alphabetically.
		return new TreeSet<Tag>( (List<Tag>) q.execute() );
	}
	
	// TODO memcache!
	private List<Script> getRecentScripts( PersistenceManager pm ) {
    	Query query = pm.newQuery( Script.class );
    	query.setOrdering( "created desc" );
    	query.setRange( 0, 10 );
		return (List<Script>)query.execute();
	}
}
