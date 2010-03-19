package org.thomnichols.pythonwebconsole.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;


@PersistenceCapable
public class Comment {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey private Long id;
	@Persistent private String author;
	@Persistent private Date created = new Date();
	@Persistent private String title;
	@Persistent private Text text;
	@Persistent private String scriptID;
	
	public Comment(String scriptID, String author, String title, String text ) {
		this.scriptID = scriptID;
		this.author = author == null || "".equals( author.trim() ) ? 
				"Anonymous" : author;
		this.title = title;
		this.text = new Text( text );
	}
	
    public String getAuthor() { return this.author; }
    public String getText() { return this.text.getValue(); }
    public Date getCreated() { return this.created; }
    public String getTitle() { return this.title; }
}
