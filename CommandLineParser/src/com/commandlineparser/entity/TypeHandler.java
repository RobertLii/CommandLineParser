package com.commandlineparser.entity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.commandlineparser.exception.ParseException;

public class TypeHandler {
	public static Object createValue(String str, Object obj) throws ParseException {
		return createValue(str, (Class<?>) obj);
	}
	
	public static Object createValue(String str, Class<?> clazz) throws ParseException {
		if (PatternOptionBuilder.STRING_VALUE == clazz) {
			return str;
		} else if (PatternOptionBuilder.OBJECT_VALUE == clazz) {
			return createObject(str);
		} else if (PatternOptionBuilder.NUMBER_VALUE == clazz) {
			return createNumber(str);
		} else if (PatternOptionBuilder.DATE_VALUE == clazz) {
			return createDate(str);
		} else if (PatternOptionBuilder.CLASS_VALUE == clazz) {
			return createClass(str);
		} else if (PatternOptionBuilder.FILE_VALUE == clazz) {
			return createFile(str);
		} else if (PatternOptionBuilder.EXISTING_FILE_VALUE == clazz) {
			return createFiles(str);
		} else if (PatternOptionBuilder.URL_VALUE == clazz) {
			return createURL(str);
		} else {
			return null;
		}
	}
	
	public static Object createObject(String className) throws ParseException {
		Class<?> cl;
		
		try {
			cl = Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			throw new ParseException("Unable to find the class " + className);
		}
		
		try {
			return cl.newInstance();
		} catch (Exception e) {
			throw new ParseException(e.getMessage());
		}
	}
	
	public static Number createNumber(String str) throws ParseException {
		try {
			if (str.indexOf('.') != -1)
				return Double.valueOf(str);
			return Long.valueOf(str);
		} catch (NumberFormatException e) {
			throw new ParseException(e.getMessage());
		}
	}
	
	public static Class<?> createClass(String str) throws ParseException {
		try {
			return Class.forName(str);
		} catch (ClassNotFoundException e) {
			throw new ParseException("Unable to find the class " + str);
		}
	}
	
	public static Date createDate(String str) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	public static URL createURL(String str) throws ParseException {
		try {
			return new URL(str);
		} catch (MalformedURLException e) {
			throw new ParseException("Unable to parse the URL " + str);
		}
	}
	
	public static File createFile(String str) {
		return new File(str);
	}
	
	public static File[] createFiles(String str) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
