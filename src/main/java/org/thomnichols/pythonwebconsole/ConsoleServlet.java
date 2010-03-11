package org.thomnichols.pythonwebconsole;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Script;

public class ConsoleServlet extends HttpServlet {
	private static final long serialVersionUID = 872325023023240055L;
	final Logger log = LoggerFactory.getLogger( getClass() );

	private PersistenceManagerFactory pmf; 
	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
	}
	
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		PersistenceManager pm = pmf.getPersistenceManager();
        try {
        	// get the permalink as the last part of the path
        	String url = req.getRequestURI();
        	String permalink = url.substring( url.lastIndexOf( "/" )+1, url.length() );
        	if ( permalink == null || permalink.length() < 1 ) {
        		resp.sendError( 400, "Hrm, something is missing here." );
        		return;
        	}
    		// lookup script based on script id/ permalink
        	Script script = pm.getObjectById( Script.class, permalink );
        	req.setAttribute( "source", script.getSource() );
    		req.getRequestDispatcher( "/index.jsp" ).forward( req, resp );
        }
        catch ( JDOObjectNotFoundException ex ) {
        	resp.sendError( 404, "Say wha?" );
        }
        finally { pm.close(); }
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
			result = ex.getMessage()!= null ? ex.getMessage() : ex.toString();
			status = "error";
		}
		
		// TODO proper escaping -- unicode & character codes?
		result = result.replaceAll("<","&lt;").replaceAll( ">","&gt;").
			replaceAll("\\\\","\\\\\\\\").replaceAll( "\"", "\\\\\\\"" );
		
		resp.setContentType( "application/json" );
		resp.getWriter().append( "{\"status\":\"" ).append(status)
			.append( "\",\"output\":\"").append(result).append( "\"}" );
	}
}
