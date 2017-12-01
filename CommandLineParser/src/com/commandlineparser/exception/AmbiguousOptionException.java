package com.commandlineparser.exception;

/**
 * 
 * Exception thrown when an opton can't be identified from a partial name.
 * 
 * @author Richard
 * @Date 2017-12-01 14:18:18
 */
public class AmbiguousOptionException extends ParseException {

	private static final long serialVersionUID = -50535924111725291L;

	public AmbiguousOptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
