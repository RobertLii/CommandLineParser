package com.commandlineparser.entity;

final class OptionValidator {
	static void validateOption(String opt) throws IllegalArgumentException {
		if (null == opt) return;
		if (opt.length() == 1) {    //handle the single character option
			char ch = opt.charAt(0);
			if (!isValidOpt(ch)) {
				throw new IllegalArgumentException("Illegal option name '" + ch + "'");
			}
		} else {    //handle the multi character option
			for (char ch : opt.toCharArray()) {
				if (!isValidChar(ch))
					throw new IllegalArgumentException("The option '" + opt + "' contains an illegal character : '" + ch + "'");
			}
		}
	}
	
	private static boolean isValidOpt(char c) {
		return isValidChar(c) || c == '?' || c == '@';
	}
	
	private static boolean isValidChar(char c) {
		return Character.isJavaIdentifierPart(c);
	}
}
