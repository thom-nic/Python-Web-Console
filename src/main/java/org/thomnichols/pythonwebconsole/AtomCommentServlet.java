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
import org.thomnichols.pythonwebconsole.model.Comment;
import org.thomnichols.pythonwebconsole.model.Script;

public class AtomCommentServlet extends HttpServlet {
	private static final long serialVersionUID = -6070148824155494429L;
	final Logger log = LoggerFactory.getLogger( getClass() );	
	private PersistenceManagerFactory pmf; 

	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			String scriptID = Util.validateParam( req, "script" );
			
			Script script = pm.getObjectById( Script.class, scriptID  );
			Query q = pm.newQuery( Comment.class );
			q.setFilter( "script == :s" );
			q.setOrdering( "created desc" );
			q.setRange( 0,30 );
			List<Comment> comments = (List<Comment>)q.execute( script );

			req.setAttribute( "script", script );
			req.setAttribute( "scriptID", scriptID );
			req.setAttribute( "comments", comments );
			req.setAttribute( "updated", comments.size() > 0 ? 
					comments.get(0).getCreatedRFC() : script.getCreatedRFC() );
			req.getRequestDispatcher( "/WEB-INF/atom-comments.xml.jsp" ).forward( req, resp );
		}
		catch ( ValidationException ex ) {
			resp.sendError( 400, "Missing or invalid script ID" );
			return;
		}
		finally { pm.close(); }
	}

	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
	}
}
