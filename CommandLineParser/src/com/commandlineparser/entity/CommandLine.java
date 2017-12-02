package com.commandlineparser.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.commandlineparser.exception.ParseException;

public class CommandLine implements Serializable {
	private static final long serialVersionUID = -132778968523808585L;
	
	/** the unrecognized options/arguments */
	private final List<String> args = new LinkedList<>();
	
	/** the processed options */
	private final List<Option> options = new ArrayList<>();
	
	public CommandLine() {}
	
	public boolean hasOption(String opt) {
		return options.contains(resolveOption(opt));
	}
	
	public boolean hasOption(char opt) {
		return hasOption(String.valueOf(opt));
	}
	
	public Object getParsedOptionValue(String opt) throws ParseException {
		String res = getOptionValue(opt);
		Option option = resolveOption(opt);
		
		if (option == null || res == null)
			return null;
		
		return TypeHandler.createValue(res, option.getType());
	}
	
	public String getOptionValue(String opt) {
		String[] values = getOptionValues(opt);
		
		return (values == null) ? null : values[0];
	}
	
	public String getOptionValue(char opt) {
		return getOptionValue(String.valueOf(opt));
	}
	
	public String[] getOptionValues(String opt) {
		List<String> values = new ArrayList<>();
		
		for (Option option : options) {
			if (opt.equals(option.getOpt()) || opt.equals(option.getLongOpt())) {
				values.addAll(option.getValuesList());
			}
		}
		
		return values.isEmpty() ? null : values.toArray(new String[values.size()]);
	}
	
	private Option resolveOption(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		
		for (Option option : options) {
			if (opt.equals(option.getOpt()))
				return option;
			if (opt.equals(option.getLongOpt()))
				return option;
		}
		
		return null;
	}
	
	public String[] getOptionValues(char opt) {
		return getOptionValues(String.valueOf(opt));
	}
	
	public String getOptionValue(String opt, String defaultValue) {
		String value = getOptionValue(opt);
		
		return (value != null) ? value : defaultValue;
	}
	
	public String getOptionValue(char opt, String defaultValue) {
		return getOptionValue(String.valueOf(opt), defaultValue);
	}
	
	public Properties getOptionProperties(String opt) {
		Properties props = new Properties();
		
		for (Option option : options) {
			if (opt.equals(option.getOpt()) || opt.equals(option.getLongOpt())) {
				List<String> values = option.getValuesList();
				if (values.size() >= 2) {
					props.put(values.get(0), values.get(1));
				} else if (values.size() == 1) {
					props.put(values.get(0), "true");
				}
			}
		}
		
		return props;
	}
	
	public String[] getArgs() {
		String[] result = new String[args.size()];
		args.toArray(result);
		
		return result;
	}
	
	public List<String> getArgList() {
		return args;
	}
	
	protected void addArg(String arg) {
		args.add(arg);
	}
	
	protected void addOption(Option opt) {
		options.add(opt);
	}
	
	public Iterator<Option> iterator() {
		return options.iterator();
	}
	
	public Option[] getOptions() {
		return options.toArray(new Option[options.size()]);
	}
	
	/**
	 * A nested builder class to create <code>CommandLine</code> instance using descriptive methods.
	 * 
	 */
	public static final class Builder {
		private final CommandLine commandLine = new CommandLine();
		
		public Builder addOption(Option opt) {
			commandLine.addOption(opt);
			return this;
		}
		
		public Builder addArg(String arg) {
			commandLine.addArg(arg);
			return this;
		}
		
		public CommandLine build() {
			return commandLine;
		}
	}
}
