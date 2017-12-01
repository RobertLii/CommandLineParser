package com.commandlineparser.exception;

/**
 * 
 * Base for the exceptions thrown during parsing of a command-line
 * 
 * @author Richard
 * @Date 2017-12-01 14:08:03
 */
public class ParseException extends Exception {	

	private static final long serialVersionUID = -7293055257487988769L;

	/**
	 * Construct a new <code>ParseException</code> with the specified detail message.
	 * @param message
	 */
	public ParseException(String message) {
		super(message);
	}
}
