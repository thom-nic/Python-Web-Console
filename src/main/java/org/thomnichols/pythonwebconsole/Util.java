package org.thomnichols.pythonwebconsole;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class Util {

	public static final DateFormat RFC_DATE_FORMAT =
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	private static final Pattern PERMALINK_PATTERN = 
		Pattern.compile( "[\\s'\"`~!@#$%^&*()_\\-\\+={}\\[\\];:,.<>/?\\\\]+" );

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
	
	/**
	 * Asserts that a parameter value is neither null nor whitespace-only.  Returns 
	 * the value or throws a {@link ValidationException}.
	 * @param req
	 * @param paramName
	 * @return
	 * @throws ValidationException
	 */
	public static String validateParam( HttpServletRequest req, String paramName ) 
				throws ValidationException {
		String param = req.getParameter( paramName );
		if ( param == null || "".equals( param.trim() ) )
			throw new ValidationException( "For parameter: " + paramName );
		return param;
	}
	
	/**
	 * Generate a string suitable for a permalink by removing URL characters 
	 * that would need to be escaped.
	 * @param title
	 * @return
	 */
    public static String generatePermalink( String title ) {
    	return PERMALINK_PATTERN.matcher( title ).replaceAll( "-" );
    }
    
    /**
     * Same as {@link #generatePermalink(String)}, but adds a pseudo-random 
     * number at the end to avoid collisions.
     */
    public static String generatePermalinkTS( String title ) {
    	return generatePermalink( title ) + "-" + 
    		( System.currentTimeMillis() % 99999 ); 
    }

    /**
     * Trims data and hashes it.
     * @param data
     * @return MD5Sum string
     * @throws RuntimeException
     */
    public static String md5Hash( String data ) throws RuntimeException {
    	if ( data == null ) return null;
    	try {
	    	MessageDigest md5 = MessageDigest.getInstance( "md5" );
	    	md5.update( data.trim().getBytes( "utf-8" ) );
	    	return String.format( "%032x", new BigInteger( 1, md5.digest() ) );
    	}
    	catch ( Exception ex ) {
    		throw new RuntimeException( "Error creating MD5 hash", ex );
    	}
    }
}
