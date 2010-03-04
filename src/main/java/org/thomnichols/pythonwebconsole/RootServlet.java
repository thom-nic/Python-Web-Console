/**
 * 
 */
package org.thomnichols.pythonwebconsole;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tnichols
 *
 */
public class RootServlet extends HttpServlet {
	final Logger log = LoggerFactory.getLogger( getClass() );
	
	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		log.debug( "------------------------RootServlet#doGet" );
		req.setAttribute( "title", "Test title" );
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
}
