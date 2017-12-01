package com.commandlineparser.entity;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;

/**
 * 
 * Allows Options to be created from a single String.
 * 
 * @author Richard
 * @Date 2017-12-01 22:12:25
 */
public class PatternOptionBuilder {
	public static final Class<String> STRING_VALUE = String.class;
	public static final Class<Object> OBJECT_VALUE = Object.class;
	public static final Class<Number> NUMBER_VALUE = Number.class;
	public static final Class<Date> DATE_VALUE = Date.class;
	public static final Class<?> CLASS_VALUE = Class.class;
	
	public static final Class<FileInputStream> EXISTING_FILE_VALUE = FileInputStream.class;
	public static final Class<File> FILE_VALUE = File.class;
	public static final Class<File[]> FILES_VALUE = File[].class;
	public static final Class<URL> URL_VALUE = URL.class;
	
	public static Object getValueClass(char ch) {
		switch (ch) {
		case '@':
			return PatternOptionBuilder.OBJECT_VALUE;
		case ':':
			return PatternOptionBuilder.STRING_VALUE;
		case '%':
			return PatternOptionBuilder.NUMBER_VALUE;
		case '+':
			return PatternOptionBuilder.CLASS_VALUE;
		case '#':
			return PatternOptionBuilder.DATE_VALUE;
		case '<':
			return PatternOptionBuilder.EXISTING_FILE_VALUE;
		case '>':
			return PatternOptionBuilder.FILE_VALUE;
		case '*':
			return PatternOptionBuilder.FILES_VALUE;
		case '/':
			return PatternOptionBuilder.URL_VALUE;
		}
		
		return null;
	}
	
	public static boolean isValueCode(char ch) {
		return ch == '@' || ch == ':'
				|| ch == '%' || ch == '+'
				|| ch == '#' || ch == '<'
				|| ch == '>' || ch == '*'
				|| ch == '/' || ch == '!';
	}
	
	public static Options parsePattern(String pattern) {
		char opt = ' ';
		boolean required = false;
		Class<?> type = null;
		Options options = new Options();
		
		for (int i = 0; i < pattern.length(); i++) {
			char ch = pattern.charAt(i);
			
			//a value code comes after an option and specifies details about it
			if (!isValueCode(ch)) {
				if (opt != ' ') {
					final Option option = Option.builder(String.valueOf(opt))
							.hasArg(type != null)
							.required(required)
							.type(type)
							.build();
					options.addOption(option);
					required = false;
					type = null;
					opt = ' ';
				}
				
				opt = ch;
			} else if (ch == '!') {
				required = true;
			} else {
				type = (Class<?>) getValueClass(ch);
			}
		}
		
		// Maybe The final option need dealing with
		if (opt != ' ') {
			final Option option = Option.builder(String.valueOf(opt))
					.hasArg(type != null)
					.required(required)
					.type(type)
					.build();
			options.addOption(option);
		}
		
		return options;
	}
}
