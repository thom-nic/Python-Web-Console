package org.thomnichols.pythonwebconsole;

public class PermalinkMissingException extends Exception {
	private static final long serialVersionUID = 1981584012591853004L;

	public PermalinkMissingException() { super(); }
	public PermalinkMissingException( String msg ) { super(msg); }
	public PermalinkMissingException( Throwable cause ) { super(cause); }
	public PermalinkMissingException( String msg, Throwable cause ) { super(msg,cause); }
}
