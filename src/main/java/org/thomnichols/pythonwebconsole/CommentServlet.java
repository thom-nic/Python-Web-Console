package org.thomnichols.pythonwebconsole;

import static org.thomnichols.pythonwebconsole.Util.validateCaptcha;
import static org.thomnichols.pythonwebconsole.Util.validateParam;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Comment;
import org.thomnichols.pythonwebconsole.model.Script;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = -7795493965633459235L;
	final Logger log = LoggerFactory.getLogger( getClass() );

	private PersistenceManagerFactory pmf;
	private String recapPrivateKey;
	private boolean debug;

	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
		this.recapPrivateKey = (String)getServletContext().getAttribute( "recaptchaPrivateKey" );
		this.debug = (Boolean)getServletContext().getAttribute( "debug" );
	}
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		if ( StringUtils.isNotBlank( req.getParameter( "junk" ) ) ) { // simple captcha
			resp.sendError( 400, "Boooo!!" );
			return;
		}
		
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
        	if ( ! debug ) validateCaptcha( recapPrivateKey, req );
			String scriptID = validateParam( req, "script_id" );
			String title = validateParam( req, "title" );
			String commentText = El.esc( validateParam( req, "content" ) );
			String author = req.getParameter( "author" );
			String email = req.getParameter( "email" );
			
			Script s = pm.getObjectById( Script.class, scriptID ); // verify script exists.
			
			Comment comment = new Comment( s, author, email, title, commentText );
			pm.makePersistent( comment );
			log.debug( "Saved comment from: " + comment.getAuthor() );
			// TODO how to represent this in the response?
			resp.setStatus( 201 );
			resp.getWriter().append( "Okey dokey." );
		}
        catch ( JDOObjectNotFoundException ex ) {
        	resp.sendError( 404, "Invalid script ID" );
        }
		catch ( ValidationException ex ) {
			log.warn( "Validation failed", ex );
			resp.sendError( 400, "Validation failed " + ex.getMessage() );
		}
		finally { pm.close(); }
	}
	
	@Override
	protected void doDelete( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {

		UserService userSvc = UserServiceFactory.getUserService();
		if ( ! userSvc.isUserLoggedIn() || ! userSvc.isUserAdmin() ) {
			resp.sendError( 401, "Uhm, no." );
			return; 
		}
		
		String commentID = req.getParameter( "cid" );
		if ( StringUtils.isBlank( commentID ) ) {
			resp.sendError( 400, "Missing comment ID" );
			return;
		}
	
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			Comment c = pm.getObjectById( Comment.class, 
					KeyFactory.stringToKey( commentID ) );
			pm.deletePersistent( c );
		}
        catch ( JDOObjectNotFoundException ex ) {
        	log.warn( "Could not find comment with ID {} to delete", commentID );
        	resp.sendError( 404, "Invalid comment ID" );
        }
		finally { pm.close(); }
	}
}
