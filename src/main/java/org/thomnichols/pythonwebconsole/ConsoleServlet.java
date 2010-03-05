package org.thomnichols.pythonwebconsole;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleServlet extends HttpServlet {
	private static final long serialVersionUID = 872325023023240055L;
	final Logger log = LoggerFactory.getLogger( getClass() );

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
