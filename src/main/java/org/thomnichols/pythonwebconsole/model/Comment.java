package org.thomnichols.pythonwebconsole.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.thomnichols.pythonwebconsole.Util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;


@PersistenceCapable
public class Comment {

    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @PrimaryKey private Key key;
	@Persistent private String permalink = "";
	@Persistent private String author;
	@Persistent private String authorEmail;
	@Persistent private String emailHash;
	@Persistent private Date created = new Date();
	@Persistent private String title;
	@Persistent private Text text;
    @Persistent private Script script;
	
	public Comment(Script parent, String author, String email, String title, String text ) {
		this.script = parent;
		this.author = author == null || "".equals( author.trim() ) ? 
				"Anonymous" : author;
		this.authorEmail = email;
		this.emailHash = Util.md5Hash( email.toLowerCase() );
		this.title = title;
		this.text = new Text( text );
		this.permalink = Util.generatePermalinkTS( this.title );
	}
	
	public String getKey() { return KeyFactory.keyToString( this.key ); }
    public String getAuthor() { return this.author; }
    public String getAuthorEmail() { return this.authorEmail; }
    public String getEmailHash() { return this.emailHash; }
    public String getText() { return this.text.getValue(); }
    public Date getCreated() { return this.created; }
    public String getTitle() { return this.title; }
    public String getPermalink() { return this.permalink; }
    public Script getScript() { return this.script; }
    public String getCreatedRFC() {
    	return Util.RFC_DATE_FORMAT.format( this.created );
    }
}
