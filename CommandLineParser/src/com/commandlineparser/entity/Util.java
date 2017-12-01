package com.commandlineparser.entity;

final class Util {
	
	/**
	 * Remove the hyphens from the beginning of <code>str</code> and then return the new string.
	 * 
	 * @param str
	 * @return
	 */
	static String stripLeadingHyphens(String str) {
		if (null == str)
			return null;
		if (str.startsWith("--"))
			return str.substring(2);
		else if (str.startsWith("-"))
			return str.substring(1);
		
		return str;
	}
	
	/**
	 * Remove the leading and trailing quotes from <code>str</code>
	 * 
	 * @param str
	 * @return
	 */
	static String stripLeadingAndTrailingQuotes(String str) {
		int length = str.length();
		if (length > 1 && str.startsWith("\"") && str.endsWith("\"") 
				&& str.substring(1, length - 1).indexOf('"') == -1) {
			return str.substring(1, length - 1);
		}
		
		return str;
	}
}
