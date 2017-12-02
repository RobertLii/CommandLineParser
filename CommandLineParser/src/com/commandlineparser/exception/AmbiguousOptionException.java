package com.commandlineparser.exception;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * Exception thrown when an opton can't be identified from a partial name.
 * 
 * @author Richard
 * @Date 2017-12-01 14:18:18
 */
public class AmbiguousOptionException extends UnrecognizedOptionException {

	private static final long serialVersionUID = -50535924111725291L;

	private final Collection<String> matchingOptions;
	
	public AmbiguousOptionException(String option, Collection<String> matchingOptions) {
		super(createMessage(option, matchingOptions), option);
		this.matchingOptions = matchingOptions;
	}
	
	public Collection<String> getMatchingOptions() {
		return matchingOptions;
	}
	
	private static String createMessage(String option, Collection<String> matchingOptions) {
		StringBuilder sb = new StringBuilder("Ambiguous option : ");
		sb.append(option);
		sb.append("'  (could be: ");
		
		for (Iterator<String> it = matchingOptions.iterator(); it.hasNext(); ) {
			sb.append("'");
			sb.append(it.next());
			sb.append("'");
			if (it.hasNext())
				sb.append(", ");
		}
		sb.append(")");
		
		return sb.toString();
	}
}
