package com.commandlineparser.exception;

/**
 * 
 * Exception thrown when an option requiring an argument was provided with no argument.
 * 
 * @author Richard
 * @Date 2017-12-01 14:27:05
 */
public class MissingArgumentException extends ParseException {

	private static final long serialVersionUID = -286196712032584189L;

	public MissingArgumentException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
