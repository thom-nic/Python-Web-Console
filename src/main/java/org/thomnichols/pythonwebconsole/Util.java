package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class Util {

	public static void validateCaptcha( String recapPrivateKey, HttpServletRequest req ) 
			throws ValidationException {
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
				post.append( "privatekey=" ).append( recapPrivateKey ) 
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
	
	public static String validateParam( HttpServletRequest req, String paramName ) 
				throws ValidationException {
		String param = req.getParameter( paramName );
		if ( param == null || "".equals( param.trim() ) )
			throw new ValidationException( "For parameter: " + paramName );
		return param;
	}
}
