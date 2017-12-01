package com.commandlineparser.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Options represents a collection of {@link Option} objects, which describe the possible options for a command-line.
 * 
 * @author Richard
 * @Date 2017-12-01 18:10:19
 */
public class Options implements Serializable {

	private static final long serialVersionUID = -4521328383296540914L;
	
	/** a map of the options with character key*/
	private final Map<String, Option> shortOpts = new LinkedHashMap<>();
	private final Map<String, Option> longOpts = new LinkedHashMap<>();
	private final Map<String, OptionGroup> optionGroups = new LinkedHashMap<>();
	private final List<Object> requiredOpts = new ArrayList<>();
	
	public Options addOptionGroup(OptionGroup group) {
		if (group.isRequired())
			requiredOpts.add(group);
		
		for (Option option : group.getOptions()) {
			option.setRequired(false);
			addOption(option);
			
			optionGroups.put(option.getKey(), group);
		}
		
		return this;
	}
	
	Collection<OptionGroup> getOptionGroups() {
		return new HashSet<OptionGroup>(optionGroups.values());
	}
	
	public Options addOption(String opt, String description) {
		addOption(opt, null, false, description);
		
		return this;
	}
	
	public Options addOption(String opt, boolean hasArg, String description) {
		addOption(opt, null, hasArg, description);
		
		return this;
	}
	
	public Options addOption(String opt, String longOpt, boolean hasArg, String description) {
		addOption(new Option(opt, longOpt, hasArg, description));
		
		return this;
	}
	
	public Options addRequiredOption(String opt, String longOpt, boolean hasArg, String description) {
		Option option = new Option(opt, longOpt, hasArg, description);
		option.setRequired(true);
		addOption(option);
		
		return this;
	}
	
	public Options addOption(Option opt) {
		String key = opt.getKey();
		
		if (opt.hasLongOpt())
			longOpts.put(opt.getLongOpt(), opt);
		if (opt.isRequired()) {
			if (requiredOpts.contains(key))
				requiredOpts.remove(requiredOpts.indexOf(key));
			requiredOpts.add(key);
		}
		
		shortOpts.put(key, opt);
		
		return this;
	}
	
	public Collection<Option> getOptions() {
		return Collections.unmodifiableCollection(helpOptions());
	}
	
	List<Option> helpOptions() {
		return new ArrayList<Option>(shortOpts.values());
	}
	
	public List getRequiredOptions() {
		return Collections.unmodifiableList(requiredOpts);
	}
	
	public Option getOption(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		
		if (shortOpts.containsKey(opt))
			return shortOpts.get(opt);
		
		return longOpts.get(opt);
	}
	
	public List<String> getMatchingOptions(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		List<String> matchingOpts = new ArrayList<>();
		
		if (longOpts.keySet().contains(opt))
			return Collections.singletonList(opt);
		
		for (String longOpt : longOpts.keySet()) {
			if (longOpt.startsWith(opt))
				matchingOpts.add(opt);
		}
		
		return matchingOpts;
	}
	
	public boolean hasOption(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		
		return shortOpts.containsKey(opt) || longOpts.containsKey(opt);
	}
	
	public boolean hasLongOption(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		
		return longOpts.containsKey(opt);
	}
	
	public boolean hasShortOption(String opt) {
		opt = Util.stripLeadingHyphens(opt);
		
		return shortOpts.containsKey(opt);
	}
	
	public OptionGroup getOptionGroup(Option opt) {
		return optionGroups.get(opt.getKey());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[ Options: [ short ");
		sb.append(shortOpts.toString());
		sb.append(" ] [ long ");
		sb.append(longOpts);
		sb.append(" ]");
		
		return sb.toString();
	}
}
