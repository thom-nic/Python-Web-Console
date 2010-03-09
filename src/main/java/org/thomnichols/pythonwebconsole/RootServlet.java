/**
 * 
 */
package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Script;

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
		this.pmf = JDOHelper.getPersistenceManagerFactory("datastore");
	}
	
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		req.setAttribute("recentScripts", getRecentScripts());
		super.getServletContext().
			getRequestDispatcher( "/index.jsp" ).forward( req, resp );
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
	
	// TODO memcache!
	private List<Script> getRecentScripts() {
		PersistenceManager pm = pmf.getPersistenceManager();
        try {
        	Query query = pm.newQuery( Script.class, "order by created desc range 10" );
    		return (List<Script>)query.execute();
        } finally { pm.close(); }
	}
}
