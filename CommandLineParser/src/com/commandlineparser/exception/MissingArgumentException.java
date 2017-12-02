package com.commandlineparser.exception;

import com.commandlineparser.entity.Option;

/**
 * 
 * Exception thrown when an option requiring an argument was provided with no argument.
 * 
 * @author Richard
 * @Date 2017-12-01 14:27:05
 */
public class MissingArgumentException extends ParseException {

	private static final long serialVersionUID = -286196712032584189L;

	private Option option;
	
	public MissingArgumentException(String message) {
		super(message);
	}
	
	public MissingArgumentException(Option option) {
		this("Missing argument for option " + option.getKey());
		this.option = option;
	}
	
	public Option getOption() {
		return option;
	}
}
