package com.commandlineparser.exception;

/**
 * 
 * Exception thrown when a required option was not provided.
 * 
 * @author Richard
 * @Date 2017-12-01 14:25:16
 */
public class MissingOptionException extends ParseException {

	private static final long serialVersionUID = 9039194998139638132L;

	public MissingOptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
