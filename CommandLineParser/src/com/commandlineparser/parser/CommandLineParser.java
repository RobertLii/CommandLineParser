package com.commandlineparser.parser;

import com.commandlineparser.entity.CommandLine;
import com.commandlineparser.entity.Options;
import com.commandlineparser.exception.ParseException;

/**
 * 
 * @author Richard
 * @Date 2017-12-01 18:20:04
 */
public interface CommandLineParser {
	
	/**
	 * 
	 * @param options the specified options
	 * @param arguments the command line arguments
	 * @return the list of atomic option and value tokens
	 * @throws ParseException
	 */
	CommandLine parse(Options options, String[] arguments) throws ParseException;
	
	/**
	 * 
	 * @param options
	 * @param arguments
	 * @param stopAtNonOption if <tt>true</tt>, an unrecognized argument stops the parsing and the remaining arguments are 
	 *     added to {@link CommandLine}s args list. If <tt>false</tt>, an unrecognized argument triggers a ParseException.
	 * @return
	 * @throws ParseException
	 */
	CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException;
}
