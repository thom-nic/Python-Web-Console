package org.thomnichols.pythonwebconsole.model;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.thomnichols.pythonwebconsole.Util;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Script {
    @PrimaryKey private String permalink;
    @Persistent private String author;
    @Persistent private Text source;
    @Persistent private Date created = new Date();
    @Persistent private String[] tags;
    @Persistent private String title;
    @Order(extensions = @Extension(vendorName="datanucleus", 
    		key="list-ordering", value="created asc"))
    @Persistent(mappedBy = "script") private List<Comment> comments;
    
    //TODO this seems to be persisted after it's generated
    private transient String rfcFormatDate = null;

    public Script(String author, String source, String title, String[] tags ) {
        this.author = author;
        this.source = new Text( source );
        this.title = title;
        this.tags = tags;
        
        this.permalink = Util.generatePermalink(this.title);
    }
    
    public void generateNewPermalink() {
    	this.permalink = Util.generatePermalinkTS(this.title); 
    }

    public String getAuthor() { return this.author; }
    public String getSource() { return this.source.getValue(); }
    public Date getCreated() { return this.created; }
    public String getTitle() { return this.title; }
    public String[] getTags() { return this.tags; }
    public List<Comment> getComments() { return this.comments; }
    public String getPermalink() { return this.permalink; }
    public String getCreatedRFC() {
    	if ( this.rfcFormatDate == null ) {
    		String dt = Util.RFC_DATE_FORMAT.format(this.created);
    		rfcFormatDate = dt.substring( 0, dt.length()-2 ) + 
    			":" + dt.substring( dt.length() -2 );
    	}
    	return rfcFormatDate;
    }
    
}