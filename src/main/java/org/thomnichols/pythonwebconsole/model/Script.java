package org.thomnichols.pythonwebconsole.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Script {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent private String author;
    @Persistent private String source;
    @Persistent private Date created;

    public Script(String author, String source, Date created) {
        this.author = author;
        this.source = source;
        this.created = created;
    }

    public Key getKey() { return this.key; }
    public String getAuthor() { return this.author; }
    public String getSource() { return this.source; }
    public Date getCreated() { return this.created; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCreated(Date date) {
        this.created = date;
    }
}