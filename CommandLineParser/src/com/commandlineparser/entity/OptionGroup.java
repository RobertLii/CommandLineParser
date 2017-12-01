package com.commandlineparser.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.commandlineparser.exception.AlreadySelectedException;

/**
 * A group of mutually exclusive options
 * @author Richard
 * @Date 2017-12-01 18:41:29
 */
public class OptionGroup implements Serializable {

	private static final long serialVersionUID = 3287667645800155901L;
	
	/** hold the options */
	private final Map<String, Option> optionMap = new LinkedHashMap<>();
	/** the name of the selected option */
	private String selected;
	/**specify whether this group is required */
	private boolean required;
	
	public OptionGroup addOption(Option option) {
		//key: option name
		//value: the option
		optionMap.put(option.getKey(), option);
		
		return this;
	}
	
	public Collection<String> getNames() {
		return optionMap.keySet();
	}
	
	public Collection<Option> getOptions() {
		return optionMap.values();
	}
	
	public void setSelected(Option option) throws AlreadySelectedException {
		if (option == null) {
			selected = null;
			return;
		}
		
		if (selected == null || selected.equals(option.getKey()))
			selected = option.getKey();
		else {
			throw new AlreadySelectedException(this, option);
		}
	}
	
	public String getSelected() {
		return selected;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Option> it = getOptions().iterator();
		sb.append("[");
		
		while (it.hasNext()) {
			Option option = it.next();
			
			if (option.getOpt() != null) {
				sb.append("-");
				sb.append(option.getOpt());
			} else {
				sb.append("--");
				sb.append(option.getLongOpt());
			}
			
			if (option.getDescription() != null) {
				sb.append(" ");
				sb.append(option.getDescription());
			}
			
			if (it.hasNext())
				sb.append(", ");
		}
		
		sb.append("]");
		return sb.toString();
	}
}
