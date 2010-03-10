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

import org.thomnichols.pythonwebconsole.model.Script;
import org.thomnichols.pythonwebconsole.model.Tag;

public class TagServlet extends HttpServlet {
	private static final long serialVersionUID = 5956964411192197317L;
	PersistenceManagerFactory pmf;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
        	String url = req.getRequestURI();
        	String tagName = url.substring( url.lastIndexOf( "/" )+1, url.length() );
        	if ( tagName == null || tagName.length() < 1 ) {
        		resp.sendError( 400, "Hrm, something is missing here." );
        		return;
        	}

        	Query q = pm.newQuery( Script.class, "tags.contains(:t)");
        	q.setOrdering("created desc");
        	q.setRange(0,20);
			List<Script> scripts = (List<Script>)q.execute(tagName);
			req.setAttribute("scripts", scripts);
			
			req.setAttribute( "tag", pm.getObjectById( Tag.class, tagName ) );
			req.getRequestDispatcher("/tag.jsp").forward(req, resp);
		}
		finally { pm.close(); }
	}
	
	@Override
	public void init() throws ServletException {
		this.pmf = (PersistenceManagerFactory)super.getServletContext().getAttribute( "persistence" );
	}
}
