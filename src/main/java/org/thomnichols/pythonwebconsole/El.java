package org.thomnichols.pythonwebconsole;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class El {
	
	private static Pattern NEWLINE = Pattern.compile( "\r?\n" );
	private static Pattern TAGS = Pattern.compile( "<|>" );
	private static DateFormat SHORT_DF = new SimpleDateFormat( "yyyy-MM-dd" );
	private static DateFormat LONG_DF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss z" );

	/**
	 * Take some text and format it so it displays nicely as HTML
	 * @param text
	 * @return
	 */
	public static String toHTML( String text ) {
		return NEWLINE.matcher( text ).replaceAll( "<br/>\n" );
	}
	
	/**
	 * More terse mechanism to escape XML since EL doesn't by default
	 * (Why oh why???) and <c:out is both verbose and makes XML editors 
	 * choke when used in an HTML attribute. 
	 * @param text
	 * @return
	 */
	public static String esc( String text ) {
		Matcher m = TAGS.matcher( text );
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "<".equals( m.group() ) ? "&lt;": "&gt;" );
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static String escURL( String text ) throws UnsupportedEncodingException {
		return URLEncoder.encode( text, "utf-8" );
	}
	
	public static String shortDate( Date dt ) {
		return SHORT_DF.format( dt );
	}
	
	public static String longDate( Date dt ) {
		return LONG_DF.format( dt );
	}
	
	public static String relativeDate( Date dt ) {
		long difference = System.currentTimeMillis() - dt.getTime();
		if ( difference < 60000 ) {
			difference = difference / 1000;
			if ( difference == 1 ) return "1 second ago";
			return "" + difference + " seconds ago";
		}
		if ( difference < 3600000 ) {
			difference = difference / 60000;
			if ( difference == 1 ) return "1 minute ago";
			return "" + difference  + " minutes ago";			
		}
		if ( difference < 86400000 ) {
			difference = difference / 3600000;
			if ( difference == 1 ) return "1 hour ago";
			return "" + difference + " hours ago";
		}

		return El.longDate( dt );  // if > 24 hours, just show date.
	}
	
	public static int size( Collection<?> c ) {
		return c.size();
	}
	
	// TODO tag for relative times, e.g. 'nn [seconds|minutes] ago'
	
}
