package com.commandlineparser.exception;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * Exception thrown when a required option was not provided.
 * 
 * @author Richard
 * @Date 2017-12-01 14:25:16
 */
public class MissingOptionException extends ParseException {

	private static final long serialVersionUID = 9039194998139638132L;

	private List missingOptions;
	
	public MissingOptionException(String message) {
		super(message);
	}
	
	public MissingOptionException(List missingOptions) {
		this(createMessage(missingOptions));
		this.missingOptions = missingOptions;
	}
	
	public List getMissingOptions() {
		return missingOptions;
	}
	
	private static String createMessage(List<?> missingOptions) {
		StringBuilder buf = new StringBuilder("Missing required option");
		buf.append(missingOptions.size() == 1 ? "" : "s");
		buf.append(": ");
		
		for (Iterator<?> it = missingOptions.iterator(); it.hasNext(); ) {
			buf.append(it.next());
			if (it.hasNext())
				buf.append(", ");
		}
		
		return buf.toString();
	}
}
