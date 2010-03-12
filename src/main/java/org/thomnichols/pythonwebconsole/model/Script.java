package org.thomnichols.pythonwebconsole.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Script {
	static final DateFormat rfcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:m:ss'Z'");
    @PrimaryKey private String permalink;
    @Persistent private String author;
    @Persistent private Text source;
    @Persistent private Date created = new Date();
    @Persistent private String[] tags;
    @Persistent private String title;
    
    private String rfcFormatDate = null;

    public Script(String author, String source, String title, String[] tags ) {
        this.author = author;
        this.source = new Text( source );
        this.title = title;
        this.tags = tags;
        
        this.permalink = generatePermalink();
    }
    
    private String generatePermalink() {
    	// TODO make static pattern object for performance
    	return this.title.replaceAll( "[\\s'\"`~!@#$%^&*()_\\-\\+={}\\[\\];:,.<>/?\\\\]+", "-" );
    }
    
    public void generateNewPermalink() {
    	this.permalink = generatePermalink() + "-" + ( System.currentTimeMillis() % 99999 ); 
    }

    public String getAuthor() { return this.author; }
    public String getSource() { return this.source.getValue(); }
    public Date getCreated() { return this.created; }
    public String getTitle() { return this.title; }
    public String[] getTags() { return this.tags; }
    public String getPermalink() { return this.permalink; }
    public String getCreatedRFC() {
    	if ( rfcFormatDate == null ) {
    		rfcFormatDate = rfcFormat.format(this.created);
//    		String dt = rfcFormat.format(this.created);
//    		rfcFormatDate = dt.substring( 0, dt.length()-2 ) + 
//    			":" + dt.substring( dt.length() -2 );
    	}
    	return rfcFormatDate;
    }
    
}