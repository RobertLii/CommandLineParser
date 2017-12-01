package com.commandlineparser.exception;

import com.commandlineparser.entity.Option;
import com.commandlineparser.entity.OptionGroup;

public class AlreadySelectedException extends ParseException {

	private static final long serialVersionUID = -3584479392029364716L;
	
	/**The option group selected*/
	private OptionGroup group;
	
	/**The option that triggered the exception*/
	private Option option;

	public AlreadySelectedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	public AlreadySelectedException(OptionGroup group, Option option) {
		this("The option '" + option.getKey() + "' was specified but an option from this group has been selected: '" + group.getSelected() + "'");
	}
	
	public OptionGroup getOptionGroup() {
		return group;
	}
	
	public Option getOption() {
		return option;
	}

}
