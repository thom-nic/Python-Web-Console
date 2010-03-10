package org.thomnichols.pythonwebconsole;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1981584012591853004L;

	public ValidationException() { super(); }
	public ValidationException( String msg ) { super(msg); }
	public ValidationException( Throwable cause ) { super(cause); }
	public ValidationException( String msg, Throwable cause ) { super(msg,cause); }
}
