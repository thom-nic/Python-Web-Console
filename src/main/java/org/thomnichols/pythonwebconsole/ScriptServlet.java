package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thomnichols.pythonwebconsole.model.Script;
import org.thomnichols.pythonwebconsole.model.Tag;

import com.google.appengine.api.urlfetch.HTTPRequest;

/**
 * This servlet handles sharing and retrieving saved scripts. 
 * TODO handle script update & delete by author?  
 * @author tnichols
 */
public class ScriptServlet extends HttpServlet {
	private static final long serialVersionUID = -2788426290753239213L;
	final Logger log = LoggerFactory.getLogger( getClass() );

	private PersistenceManagerFactory pmf;
	private String recapPublicKey;
	private String recapPrivateKey;
	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
		this.recapPrivateKey = super.getInitParameter( "recaptchaPublicKey" );
		this.recapPrivateKey = super.getInitParameter( "recaptchaPrivateKey" );
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
        	this.validateCaptcha( req );
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
	
	protected void validateCaptcha( HttpServletRequest req ) throws ValidationException {
		try {
			URLConnection recaptcha = new URL( "http://api-verify.recaptcha.net/verify" )
				.openConnection();
			recaptcha.setDoOutput( true );
			try {
				String challengeParam = URLEncoder.encode( validateParam( req,  
						"recaptcha_challenge_field" ), "utf-8" );
				String responseParam = URLEncoder.encode( validateParam( req, 
					"recaptcha_response_field" ), "utf-8" );
				StringBuffer post = new StringBuffer();
				post.append( "privatekey=" ).append( this.recapPrivateKey ) 
						.append( "&remoteip=" ).append( req.getRemoteAddr() )
						.append( "&challenge=" ).append( challengeParam )
						.append( "&response=" ).append( responseParam );
				IOUtils.write( post.toString(), recaptcha.getOutputStream(), "utf-8" );
				List<String> response = IOUtils.readLines( recaptcha.getInputStream(), "utf-8" );
				if ( ! "true".equalsIgnoreCase( response.get(0) ) ) 
					throw new ValidationException( "Invalid captcha response: "
							+ response.get(1) );
			}
			finally {
				recaptcha.getOutputStream().close();
				recaptcha.getInputStream().close();
			}
		}
		catch ( IOException ex ) {
			throw new ValidationException( ex );
		}
	}
	
	protected String validateParam( HttpServletRequest req, String paramName ) 
			throws ValidationException {
		String param = req.getParameter( paramName );
		if ( param == null || "".equals( param.trim() ) )
			throw new ValidationException( "For parameter: " + paramName );
		return param;
	}
}
