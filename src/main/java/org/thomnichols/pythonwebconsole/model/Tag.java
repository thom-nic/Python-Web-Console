package org.thomnichols.pythonwebconsole.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Tag implements Comparable<Tag> {
    @PrimaryKey private String name;
    @Persistent private long count;
    transient String scale;

    public Tag( String name, long count ) {
		this.name = name;
		this.count = count;
	}
    
    public String getName() { return this.name; }
    public long getCount() { return this.count; }
    public void setCount( long count ) { this.count = count; }
    public String getScale() { return this.scale; }
    public void setScale( String scale ) { this.scale = scale; };
    
	@Override
	public int compareTo(Tag o) {
		String other = o == null ? "" : o.getName();
		if ( other == null ) other = "";
		return this.name.compareTo(other);
	}
	
	@Override
	public boolean equals(Object obj) {
		return ( obj instanceof Tag ) && this.compareTo((Tag)obj) == 0;
	}
}
