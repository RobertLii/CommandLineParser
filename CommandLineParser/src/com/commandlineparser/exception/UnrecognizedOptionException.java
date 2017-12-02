package com.commandlineparser.exception;

/**
 * Exception thrown during parsing signaling an unrecognized option was seen.
 * 
 * @author Richard
 * @Date 2017-12-01 14:20:41
 */
public class UnrecognizedOptionException extends ParseException {

	private static final long serialVersionUID = 2427953281143500697L;

	private String option;
	
	public UnrecognizedOptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public UnrecognizedOptionException(String message, String option) {
		this(message);
		this.option = option;
	}
	
	public String getOption() {
		return option;
	}
}
