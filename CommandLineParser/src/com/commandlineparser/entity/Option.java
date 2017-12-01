package com.commandlineparser.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a single command-line option
 * 
 * @author Richard
 * @Date 2017-12-01 18:09:15
 */
public class Option implements Cloneable, Serializable {

	private static final long serialVersionUID = 3334058452808485312L;
	public static final int UNINITIALIZED = -1;
	public static final int UNLIMITED_VALUES = -2;
	
	private final String opt;    //the name of the option, also is short-option name.
	private String longOpt;    //the long representation of the option
	private String argName;    //the name of the argument for the option
	private String description;
	private boolean required;
	private boolean optionalArg;    //specifies whether the argument value of the option is optional
	private int numberOfArgs = UNINITIALIZED;
	
	private Class<?> type = String.class;
	private List<String> values = new ArrayList<>();
	private char valueSeparator;
	
	private Option(final Builder builder) {
		this.argName = builder.argName;
		this.description = builder.description;
		this.longOpt = builder.longOpt;
		this.numberOfArgs = builder.numberOfArgs;
		this.opt = builder.opt;
		this.optionalArg = builder.optionalArg;
		this.required = builder.required;
		this.type = builder.type;
		this.valueSeparator = builder.valueSeparator;
	}
	
	public Option(String opt, String description) throws IllegalArgumentException {
		this(opt, null, false, description);
	}
	
	public Option(String opt, boolean hasArg, String description) throws IllegalArgumentException {
		this(opt, null, hasArg, description);
	}
	
	public Option(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
		OptionValidator.validateOption(opt);
		this.opt = opt;
		this.longOpt = longOpt;
		
		if (hasArg)
			this.numberOfArgs = 1;
		this.description = description;
	}
	
	public int getId() {
		return getKey().charAt(0);
	}
	
	public String getKey() {
		return (opt == null) ? longOpt : opt;
	}
	
	public String getOpt() {
		return opt;
	}
	
	public Object getType() {
		return type;
	}
	
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	public String getLongOpt() {
		return longOpt;
	}

	public void setLongOpt(String longOpt) {
		this.longOpt = longOpt;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean hasOptionalArg() {
		return optionalArg;
	}
	
	public boolean hasLongOpt() {
		return longOpt != null;
	}
	
	public boolean hasArg() {
		return numberOfArgs > 0 || numberOfArgs == UNLIMITED_VALUES;
	}
	
	public boolean hasValueSeparator() {
		return valueSeparator > 0;
	}
	
	public char getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(char valueSeparator) {
		this.valueSeparator = valueSeparator;
	}

	public int getNumberOfArgs() {
		return numberOfArgs;
	}

	public void setNumberOfArgs(int numberOfArgs) {
		this.numberOfArgs = numberOfArgs;
	}

	public boolean hasArgs() {
		return numberOfArgs > 1 || numberOfArgs == UNLIMITED_VALUES;
	}

	public String getArgName() {
		return argName;
	}

	public void setArgName(String argName) {
		this.argName = argName;
	}
	
	public boolean hasArgName() {
		return argName != null && argName.length() > 0; 
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setOptionalArg(boolean optionalArg) {
		this.optionalArg = optionalArg;
	}
	
	void addValueForProcessing(String value) {
		if (numberOfArgs == UNINITIALIZED) {
			throw new RuntimeException("NO_ARGS_ALLOWED");
		}
		
		processValue(value);
	}
	
	private void processValue(String value) {
		if (hasValueSeparator()) {
			char separator = getValueSeparator();
			int index = value.indexOf(separator);
			
			while (index != -1) {
				if (values.size() == numberOfArgs - 1) 
					break;
				add(value.substring(0, index));
				
				value = value.substring(index + 1);
				index = value.indexOf(separator);
			}
			
			add(value);
		}
	}
	
	private void add(String value) {
		if (!acceptsArg())
			throw new RuntimeException("Cannot add value, list full.");
		
		values.add(value);
	}
	
	public String getValue() {
		return hasNoValues() ? null : values.get(0);
	}
	
	public String getValue(int index) {
		return hasNoValues() ? null : values.get(index);
	}
	
	public String getValue(String defaultValue) {
		String value = getValue();
		
		return (value != null) ? value : defaultValue;
	}
	
	public String[] getValues() {
		return hasNoValues() ? null : values.toArray(new String[values.size()]);
	}
	
	public List<String> getValuesList() {
		return values;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder().append("[ option: ");
		sb.append(opt);
		if (longOpt != null) {
			sb.append(" ").append(longOpt);
		}
		
		sb.append(" ");
		
		if (hasArgs())
			sb.append("[ARG...]");
		else if (hasArg())
			sb.append(" [ARG]");
		sb.append(" :: ").append(description);
		
		if (type != null)
			sb.append(" :: ").append(type);
		
		sb.append(" ]");
		
		return sb.toString();
	}
	
	private boolean hasNoValues() {
		return values.isEmpty();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		Option option = (Option) o;
		
		if (opt != null ? !opt.equals(option.opt) : option.opt != null)
			return false;
		if (longOpt != null ? !longOpt.equals(option.longOpt) : option.longOpt != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result;
		result = opt != null ? opt.hashCode() : 0;
		result = 31 * result + (longOpt != null ? longOpt.hashCode() : 0);
		
		return result;
	}
	
	@Override
	public Object clone() {
		try {
			Option option = (Option) super.clone();
			option.values = new ArrayList<>(values);
			
			return option;
		} catch (CloneNotSupportedException cnse) {
			throw new RuntimeException("A CloneNotSupportedException was thrown : " + cnse.getMessage());
		}
	}
	
	void clearValues() {
		values.clear();
	}
	
	boolean acceptsArg() {
		return (hasArg() || hasArgs() || hasOptionalArg()) && (numberOfArgs <= 0 || values.size() < numberOfArgs);
	}
	
	public static Builder builder() {
		return builder(null);
	}
	
	public static Builder builder(String opt) {
		return new Builder(opt);
	}
	
	public static final class Builder {
		private final String opt;
		private String description;
		private String longOpt;
		private String argName;
		private boolean required;
		private boolean optionalArg;
		private int numberOfArgs = UNINITIALIZED;
		private Class<?> type = String.class;
		private char valueSeparator;
		
		private Builder(final String opt) throws IllegalArgumentException {
			OptionValidator.validateOption(opt);
			this.opt = opt;
		}
		
		public Builder argName(final String argName) {
			this.argName = argName;
			return this;
		}
		
		public Builder desc(final String description) {
			this.description = description;
			return this;
		}
		
		public Builder longOpt(final String longOpt) {
			this.longOpt = longOpt;
			return this;
		}
		
		public Builder numberOfArgs(final int numberOfArgs) {
			this.numberOfArgs = numberOfArgs;
			return this;
		}
		
		public Builder optionalArg(final boolean isOptional) {
			this.optionalArg = isOptional;
			return this;
		}
		
		public Builder required() {
			return required(true);
		}
		
		public Builder required(final boolean required) {
			this.required = required;
			return this;
		}
		
		public Builder type(final Class<?> type) {
			this.type = type;
			return this;
		}
		
		public Builder valueSeparator() {
			return valueSeparator('=');
		}
		
		public Builder valueSeparator(final char sep) {
			this.valueSeparator = sep;
			return this;
		}
		
		public Builder hasArg() {
			return hasArg(true);
		}
		
		public Builder hasArg(final boolean hasArg) {
			numberOfArgs = hasArg ? 1 : Option.UNINITIALIZED;
			return this;
		}
		
		public Builder hasArgs() {
			numberOfArgs = Option.UNLIMITED_VALUES;
			return this;
		}
		
		public Option build() {
			if (opt == null && longOpt == null) {
				throw new IllegalArgumentException("Either opt or longOpt must be specified");
			}
			
			return new Option(this);
		}
	}
}
