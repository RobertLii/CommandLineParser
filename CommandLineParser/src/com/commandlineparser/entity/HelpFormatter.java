package com.commandlineparser.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A formatter of help message for command line options.
 * 
 * @author Richard
 * @Date 2017-12-02 00:25:49
 */
public class HelpFormatter {
	public static final int DEFAULT_WIDTH = 74;
	public static final int DEFAULT_LEFT_PAD = 1;
	public static final int DEFAULT_DESC_PAD = 3;
	public static final String DEFAULT_SYNTAX_PREFIX = "usage: ";
	public static final String DEFAULT_OPT_PREFIX = "-";
	public static final String DEFAULT_LONG_OPT_PREFIX = "--";
	public static final String DEFAULT_LONG_OPT_SEPARATOR = " ";
	public static final String DEFAULT_ARG_NAME = "arg";
	
	private int defaultWidth = DEFAULT_WIDTH;
	private int defaultLeftPad = DEFAULT_LEFT_PAD;
	private int defaultDescPad = DEFAULT_DESC_PAD;
	private String defaultSyntaxPrefix = DEFAULT_SYNTAX_PREFIX;
	private String defaultOptPrefix = DEFAULT_OPT_PREFIX;
	private String defaultLongOptPrefix = DEFAULT_LONG_OPT_PREFIX;
	private String defaultArgName = DEFAULT_ARG_NAME;
	private String longOptSeparator = DEFAULT_LONG_OPT_SEPARATOR;
	
	private String defaultNewLine = System.getProperty("line.separator");	
	protected Comparator<Option> optionComparator = new OptionComparator();
	
	/*---------getter and setter methods start-----------*/
	public int getWidth() {
		return defaultWidth;
	}

	public void setWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public int getLeftPadding() {
		return defaultLeftPad;
	}

	public void setLeftPadding(int defaultLeftPad) {
		this.defaultLeftPad = defaultLeftPad;
	}

	public int getDescPadding() {
		return defaultDescPad;
	}

	public void setDescPadding(int defaultDescPad) {
		this.defaultDescPad = defaultDescPad;
	}

	public String getSyntaxPrefix() {
		return defaultSyntaxPrefix;
	}

	public void setSyntaxPrefix(String defaultSyntaxPrefix) {
		this.defaultSyntaxPrefix = defaultSyntaxPrefix;
	}

	public String getOptPrefix() {
		return defaultOptPrefix;
	}

	public void setOptPrefix(String defaultOptPrefix) {
		this.defaultOptPrefix = defaultOptPrefix;
	}

	public String getLongOptPrefix() {
		return defaultLongOptPrefix;
	}

	public void setLongOptPrefix(String defaultLongOptPrefix) {
		this.defaultLongOptPrefix = defaultLongOptPrefix;
	}

	public String getArgName() {
		return defaultArgName;
	}

	public void setArgName(String defaultArgName) {
		this.defaultArgName = defaultArgName;
	}

	public String getLongOptSeparator() {
		return longOptSeparator;
	}

	public void setLongOptSeparator(String longOptSeparator) {
		this.longOptSeparator = longOptSeparator;
	}

	public String getNewLine() {
		return defaultNewLine;
	}

	public void setNewLine(String defaultNewLine) {
		this.defaultNewLine = defaultNewLine;
	}

	public Comparator<Option> getOptionComparator() {
		return optionComparator;
	}

	public void setOptionComparator(Comparator<Option> optionComparator) {
		this.optionComparator = optionComparator;
	}	
	/*---------getter/setter methods end------------*/

	public void printHelp(String cmdLineSyntax, Options options) {
		printHelp(getWidth(), cmdLineSyntax, null, options, null, false);
	}
	
	public void printHelp(String cmdLineSyntax, Options options, boolean autoUsage) {
		printHelp(getWidth(), cmdLineSyntax, null, options, null, autoUsage);
	}
	
	public void printHelp(String cmdLineSyntax, String header, Options options, String footer) {
		printHelp(cmdLineSyntax, header, options, footer, false);
	}
	
	public void printHelp(String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
		printHelp(getWidth(), cmdLineSyntax, header, options, footer, autoUsage);
	}
	
	public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer) {
		printHelp(width, cmdLineSyntax, header, options, footer, false);
	}
	
	public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
		PrintWriter pw = new PrintWriter(System.out);
		
		printHelp(pw, width, cmdLineSyntax, header, options, getLeftPadding(), getDescPadding(), footer, autoUsage);
		pw.flush();		
	}
	
	public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer) {
		printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, false);
	}
	
	public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer, boolean autoUsage) {
		if (cmdLineSyntax == null || cmdLineSyntax.length() == 0)
			throw new IllegalArgumentException("cmdLineSyntax not provided");
		if (autoUsage)
			printUsage(pw, width, cmdLineSyntax, options);
		else
			printUsage(pw, width, cmdLineSyntax);
		if (header != null && header.trim().length() > 0)
			printWrapped(pw, width, header);
		
		printOptions(pw, width, options, leftPad, descPad);
		
		if (footer != null && footer.trim().length() > 0)
			printWrapped(pw, width, footer);
	}
	
	public void printUsage(PrintWriter pw, int width, String app, Options options) {
		StringBuilder sb = new StringBuilder(getSyntaxPrefix()).append(app).append(" ");
		
		Collection<OptionGroup> processedGroups = new ArrayList<>();
		List<Option> optList = new ArrayList<>(options.getOptions());
		
		if (getOptionComparator() != null) {
			Collections.sort(optList, getOptionComparator());
		}
		for (Iterator<Option> it = optList.iterator(); it.hasNext(); ) {
			Option option = it.next();
			OptionGroup group = options.getOptionGroup(option);
			
			if (group != null) {
				if (!processedGroups.contains(group)) {
					processedGroups.add(group);
					
					appendOptionGroup(sb, group);
				}
				//otherwise the option was displayed in the group previously so ignore it.
			} else {
				appendOption(sb, option, option.isRequired());
			}
			
			if (it.hasNext())
				sb.append(" ");
		}
		
		printWrapped(pw, width, sb.toString().indexOf(' ') + 1, sb.toString());
	}
	
	private void appendOptionGroup(StringBuilder sb, OptionGroup group) {
		if (!group.isRequired())
			sb.append("[");
		List<Option> optList = new ArrayList<>(group.getOptions());
		if (getOptionComparator() != null)
			Collections.sort(optList, getOptionComparator());
		for (Iterator<Option> it = optList.iterator(); it.hasNext(); ) {
			appendOption(sb, it.next(), true);
			
			if (it.hasNext())
				sb.append(" | ");
		}
		
		if (!group.isRequired())
			sb.append("]");
	}
	
	private void appendOption(StringBuilder sb, Option option, boolean required) {
		if (!required)
			sb.append("[");
		
		if (option.getOpt() != null)
			sb.append("-").append(option.getOpt());
		else
			sb.append("--").append(option.getLongOpt());
		if (option.hasArg() && (option.getArgName() == null || option.getArgName().length() != 0) ) {
			sb.append(option.getOpt() == null ? longOptSeparator : " ");
			sb.append("<").append(option.getArgName() != null ? option.getArgName() : getArgName()).append(">");
		}
		
		if (!required)
			sb.append("]");
	}
	
	public void printUsage(PrintWriter pw, int width, String cmdLineSyntax) {
		int argPos = cmdLineSyntax.indexOf(' ') + 1;
		
		printWrapped(pw, width, getSyntaxPrefix().length() + argPos, getSyntaxPrefix() + cmdLineSyntax);
	}
	
	public void printOptions(PrintWriter pw, int width, Options options, int leftPad, int descPad) {
		StringBuilder sb = new StringBuilder();
		
		renderOptions(sb, width, options, leftPad, descPad);
		pw.println(sb.toString());
	}
	
	public void printWrapped(PrintWriter pw, int width, String text) {
		printWrapped(pw, width, 0, text);
	}
	
	public void printWrapped(PrintWriter pw, int width, int nextLineTabStop, String text) {
		StringBuilder sb = new StringBuilder(text.length());
		
		renderWrappedTextBlock(sb, width, nextLineTabStop, text);
		pw.println(sb.toString());
	}
	
	protected StringBuilder renderOptions(StringBuilder sb, int width, Options options, int leftPad, int descPad) {
		final String lpad = createPadding(leftPad);
		final String dpad = createPadding(descPad);
		
		int max = 0;
		List<StringBuilder> prefixList = new ArrayList<>();
		List<Option> optList = options.helpOptions();
		
		if (getOptionComparator() != null)
			Collections.sort(optList, getOptionComparator());
		
		for (Option option : optList) {
			StringBuilder optBuf = new StringBuilder();
			
			if (option.getOpt() == null) {
				optBuf.append(lpad).append("   ").append(getLongOptPrefix()).append(option.getLongOpt());
			} else {
				optBuf.append(lpad).append(getOptPrefix()).append(option.getOpt());
				if (option.hasLongOpt())
					optBuf.append(',').append(getLongOptPrefix()).append(option.getLongOpt());
			}
			
			if (option.hasArg()) {
				String argName = option.getArgName();
				if (argName != null && argName.length() == 0) {
					optBuf.append(' ');
				} else {
					optBuf.append(option.hasLongOpt() ? longOptSeparator : " ");
					optBuf.append("<").append(argName != null ? argName : getArgName()).append(">");
				}
			}
			
			prefixList.add(optBuf);
			max = optBuf.length() > max ? optBuf.length() : max;
		}
		
		int x = 0;
		for (Iterator<Option> it = optList.iterator(); it.hasNext(); ) {
			Option option = it.next();
			StringBuilder optBuf = new StringBuilder(prefixList.get(x++).toString());
			
			if (optBuf.length() < max) {
				optBuf.append(createPadding(max - optBuf.length()));
			}
			
			optBuf.append(dpad);
			int nextLineTabStop = max + descPad;
			
			if (option.getDescription() != null)
				optBuf.append(option.getDescription());
			renderWrappedText(sb, width, nextLineTabStop, optBuf.toString());
			
			if (it.hasNext())
				sb.append(getNewLine());
		}
		
		return sb;
	}
	
	protected StringBuilder renderWrappedText(StringBuilder sb, int width, int nextLineTabStop, String text) {
		int pos = findWrapPos(text, width, 0);
		
		if (pos == -1) {
			sb.append(rtrim(text));
			return sb;
		}
		
		sb.append(rtrim(text.substring(0, pos))).append(getNewLine());
		
		if (nextLineTabStop >= width)
			nextLineTabStop = 1;
		
		final String padding = createPadding(nextLineTabStop);
		
		while (true) {
			text = padding + text.substring(pos).trim();
			pos = findWrapPos(text, width, 0);
			
			if (pos == -1) {
				sb.append(text);
				return sb;
			}
			
			if (text.length() > width && pos == nextLineTabStop - 1) {
				pos = width;
			}
			
			sb.append(rtrim(text.substring(0, pos))).append(getNewLine());
		}
	}
	
	private Appendable renderWrappedTextBlock(StringBuilder sb, int width, int nextLineTabStop, String text) {
		try {
			BufferedReader br = new BufferedReader(new StringReader(text));
			String line;
			boolean firstLine = true;
			while ((line = br.readLine()) != null) {
				if (!firstLine)
					sb.append(getNewLine());
				else
					firstLine = false;
				renderWrappedText(sb, width, nextLineTabStop, line);
			}
		} catch (IOException e) {    //cannot happen
			e.printStackTrace();
		}
		
		return sb;
	}
	
	protected int findWrapPos(String text, int width, int startPos) {
		int pos = text.indexOf('\n', startPos);
		if (pos != -1 && pos <= width)
			return pos + 1;
		
		pos = text.indexOf('\t', startPos);
		if (pos != -1 && pos <= width)
			return pos + 1;
		
		if (startPos + width >= text.length())
			return -1;
		
		//look for the last whitespace character before startPost + width
		for (pos = startPos + width; pos >= startPos; --pos) {
			final char c = text.charAt(pos);
			if (c == ' ' || c == '\n' || c == '\r')
				break;
		}
		
		//if found, return
		if (pos > startPos)
			return pos;
		
		//if not found, simply chop at startPos + width
		pos = startPos + width;
		
		return pos == text.length() ? -1 : pos;
	}
	
	protected String createPadding(int len) {
		char[] padding = new char[len];
		Arrays.fill(padding, ' ');
		
		return new String(padding);
	}
	
	protected String rtrim(String s) {
		if (s == null || s.length() == 0)
			return s;
		
		int pos = s.length();
		
		while (pos > 0 && Character.isWhitespace(s.charAt(pos - 1)))
			--pos;
		return s.substring(0, pos);
	}
	
	private static class OptionComparator implements Comparator<Option>, Serializable {

		private static final long serialVersionUID = 6570350914735724203L;

		@Override
		public int compare(Option opt1, Option opt2) {
			return opt1.getKey().compareToIgnoreCase(opt2.getKey());
		}
		
	}
}
