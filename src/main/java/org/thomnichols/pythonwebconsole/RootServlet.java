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
	private String baseURL;
	private boolean debug = false;
	
	@Override
	public void init() throws ServletException {
		this.debug = Boolean.parseBoolean( super.getInitParameter( "debug" ) );
		super.getServletContext().setAttribute( "debug", debug );
		this.baseURL = super.getInitParameter( "baseURL" ) + 
			getServletContext().getContextPath(); 
		super.getServletContext().setAttribute( "baseURL", baseURL );
		this.debug = (Boolean)getServletContext().getAttribute( "debug" );
		
		this.pmf = JDOHelper.getPersistenceManagerFactory("datastore");
		super.getServletContext().setAttribute( "persistence", pmf );
	}

	@Override
	protected void doGet( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String requestURL = req.getRequestURL().toString();
		if ( ! debug && ! requestURL.startsWith( this.baseURL ) 
				&& ! requestURL.contains( ".latest." ) ) {
			resp.sendRedirect( baseURL );
			return;
		}
		
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
		List<Tag> tags = (List<Tag>) q.execute();
		if ( tags.size() > 0 ) {
			long max = tags.get(0).getCount();
			float min = tags.get( tags.size()-1 ).getCount();
			for ( Tag t : tags ) {
				// if all tags have same count, avoid divide-by-zero
		        // Create a float scale between 1 and 1.8
				double scale = ( max-min == 0 ? 1 :  (t.getCount()-min)/(max-min)*.8+1 );
				t.setScale( String.format("%.1fem",scale) );
			}
		}
		// reorder tags alphabetically.
		return new TreeSet<Tag>( tags );
	}
	
	// TODO memcache!
	private List<Script> getRecentScripts( PersistenceManager pm ) {
    	Query query = pm.newQuery( Script.class );
    	query.setOrdering( "created desc" );
    	query.setRange( 0, 10 );
		return (List<Script>)query.execute();
	}
}
