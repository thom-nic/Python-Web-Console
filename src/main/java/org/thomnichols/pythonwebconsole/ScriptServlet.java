package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Script;

/**
 * This servlet handles sharing and retrieving saved scripts. 
 * @author tnichols
 */
public class ScriptServlet extends HttpServlet {
	private static final long serialVersionUID = -2788426290753239213L;
	final Logger log = LoggerFactory.getLogger( getClass() );

	private PersistenceManagerFactory pmf; 
	
	@Override
	public void init() throws ServletException {
		// TODO add this to the servlet context if any other servlets need to 
		// access the datastore 
		this.pmf = JDOHelper.getPersistenceManagerFactory("datastore");
//		getServletContext().setAttribute( "persistence", pmf );
//		this.pmf = (PersistenceManagerFactory) getServletContext().getAttribute( "persistence" );
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
        	req.setAttribute( "script", script );
    		req.getRequestDispatcher( "/script.jsp" ).forward( req, resp );
        }
        catch ( JDOObjectNotFoundException ex ) {
        	resp.sendError( 404, "Say wha?" );
        }
        finally { pm.close(); }
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
        try {
        	// TODO validate input
        	String author = req.getParameter( "author" );
        	String source = req.getParameter( "source" );
        	String title = req.getParameter( "title" );
        	
        	String tagString = req.getParameter( "tags" );
        	if ( tagString == null ) tagString = "";
        	String[] tags = tagString.split( "\\s" );
        	
        	Script script = new Script( author, source, title, tags );
        	tx.begin();
        	Query query = pm.newQuery( Script.class, "permalink == p");
        	query.declareParameters( "String p" );
        	// handle conflicts if permalink already exists
        	while ( ((List<?>)query.execute( script.getPermalink() )).size() > 0 )
        		script.generateNewPermalink();
        	log.debug( "Saving new script with permalink: {}", script.getPermalink() );
            pm.makePersistent( script );
            tx.commit();
            // Send a 201:Created response for Ajax requests.
        	if ( "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) ) {
        		resp.setStatus(201);
        		resp.setHeader( "Location", getServletContext().getContextPath() 
        				+ "/script/" + script.getPermalink() );
        		return;
        	}
        		
            resp.sendRedirect( "/script/"+script.getPermalink() );
        }
        catch ( Exception e ) {
        	log.warn( "Unexpected exception while saving script", e );
			// TODO proper error handling
			resp.sendError( 500, "Unexpected error: " + e.getMessage() );
        } finally {
        	if ( tx.isActive() ) {
        		log.warn( "Rolling back tx!" );
        		tx.rollback();
        	}
        	pm.close();
        }
	}
}
