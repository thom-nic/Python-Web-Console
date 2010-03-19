package org.thomnichols.pythonwebconsole;

import static org.thomnichols.pythonwebconsole.Util.validateCaptcha;
import static org.thomnichols.pythonwebconsole.Util.validateParam;

import java.io.IOException;
import java.util.List;

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
import org.thomnichols.pythonwebconsole.model.Comment;
import org.thomnichols.pythonwebconsole.model.Script;
import org.thomnichols.pythonwebconsole.model.Tag;

/**
 * This servlet handles sharing and retrieving saved scripts. 
 * TODO handle script update & delete by author?  
 * @author tnichols
 */
public class ScriptServlet extends HttpServlet {
	private static final long serialVersionUID = -2788426290753239213L;
	final Logger log = LoggerFactory.getLogger( getClass() );

	private PersistenceManagerFactory pmf;
	private String recapPrivateKey;
	private boolean debug = false;
	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
		this.recapPrivateKey = (String)getServletContext().getAttribute( "recaptchaPrivateKey" );
		this.debug = (Boolean)getServletContext().getAttribute( "debug" );
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
        	req.setAttribute( "script", pm.getObjectById( Script.class, permalink  ) );
        	req.setAttribute( "comments", getComments( pm, permalink ) );
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
        	if ( ! debug ) validateCaptcha( recapPrivateKey, req );
        	String author = validateParam( req, "author" );
        	String source = validateParam( req, "source" );
        	String title = validateParam( req, "title" );

        	String tagString = req.getParameter( "tags" );
        	if ( tagString == null ) tagString = "";
        	String[] tags = tagString.split( "[\\s,]+" );
        	
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

            // update tag count:
            for ( String tagName : script.getTags() ) {
	            try {
	    			Tag tag = pm.getObjectById(Tag.class, tagName);
	    			tag.setCount( tag.getCount() + 1 );
	    		}
	    		catch ( JDOObjectNotFoundException ex ) {
	    			Tag tag = new Tag(tagName, 1);
	    			pm.makePersistent(tag);
	    		}
            }

            // Send a 201:Created response for Ajax requests.
        	if ( "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) ) {
        		resp.setStatus(201);
        		resp.setHeader( "Location", getServletContext().getContextPath() 
        				+ "/script/" + script.getPermalink() );
        		return;
        	}
	
            resp.sendRedirect( "/script/"+script.getPermalink() );
        }
        catch ( ValidationException ex ) {
        	log.warn( "Validation failed", ex );
			resp.sendError( 400, "Validation failed  " + ex.getMessage() );
        }
        catch ( Exception e ) {
        	log.warn( "Unexpected exception while saving script", e );
			resp.sendError( 500, "Unexpected error: " + e.getMessage() );
        } finally {
        	if ( tx.isActive() ) {
        		log.warn( "Rolling back tx!" );
        		tx.rollback();
        	}
        	pm.close();
        }
	}

	// TODO memcache!
	private List<Comment> getComments( PersistenceManager pm, String permalink ) {
    	Query query = pm.newQuery( Comment.class );
    	query.setFilter( "scriptID==:s" );
    	query.setOrdering( "created asc" );
    	query.setRange( 0, 100 );
		return (List<Comment>)query.execute(permalink);
	}
}
