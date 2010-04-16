package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 7919862307109350823L;
	private String adminEmail;
	
	@Override
	protected void doPost( HttpServletRequest req, HttpServletResponse resp )
			throws ServletException, IOException {
		String captcha = req.getParameter( "junk" ); 
		if ( captcha != null && ! captcha.trim().equals("") ) { // simple captcha
			resp.sendError( 400, "Boooo!!" );
			return;
		}
		
		Session session = Session.getDefaultInstance(new Properties(), null);

        try {
            Message msg = new MimeMessage( session );
            msg.setFrom( new InternetAddress( adminEmail, "Python Web Console" ) );
            msg.addRecipient( Message.RecipientType.TO,
                             new InternetAddress( adminEmail, "" ) );
            msg.setSubject( "Script reported by " + req.getParameter( "fromEmail" ) );
            msg.setText( "A user has reported the following script:\n" 
            		+ req.getParameter( "scriptLink" ) + "\n\nMessage:\n\n" 
            		+ req.getParameter( "issue" ) );
            Transport.send(msg);
        }
        catch ( MessagingException ex ) {
            resp.sendError( 500, ex.getMessage() );
            return;
        }
		resp.setContentType( "text/plain" );
		resp.getWriter().append( "Thank you; this script has been reported to the administrator" );
	}
	
	@Override
	public void init() throws ServletException {
		this.adminEmail = super.getInitParameter( "adminEmail" );
		if ( adminEmail == null ) throw new RuntimeException( 
				"Missing init param 'adminEmail' for ReportServlet" ); 
	}
}
