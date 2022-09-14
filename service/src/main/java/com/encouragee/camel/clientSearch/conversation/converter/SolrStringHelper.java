package com.encouragee.camel.clientSearch.conversation.converter;

import java.util.regex.Pattern;

final class SolrStringHelper {
	private static final Pattern CONSECUTIVE_OP_PATTERN = Pattern.compile("\\s+[+-](?:\\s*[+-]+)+");
	private static final Pattern DANGLING_OP_PATTERN = Pattern.compile("\\s+[-+\\s]+$");

	private SolrStringHelper() {}

	static CharSequence sanitizeString(String value) {
		// note: stripIllegalOperators currently doesn't work if string starts with an illegal operator
		return partialEscape(stripIllegalOperators(" " + stripUnbalancedQuotes(value))).toString().trim();
	}

	/**
	 * Escapes all special characters except '"', '-', and '+'
	 *
	 * this is copy paste from org.apache.solr.util.SolrPluginUtils as solr takes huge amount of libraries.
	 */
	private static CharSequence partialEscape(CharSequence s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' || c == '!' || c == '(' || c == ')' ||
					c == ':' || c == '^' || c == '[' || c == ']' || c == '/' ||
					c == '{' || c == '}' || c == '~' || c == '*' || c == '?'
			) {
				sb.append('\\');
			}
			sb.append(c);
		}
		return sb;
	}

	/**
	 * Strips operators that are used illegally, otherwise returns its
	 * input.  Some examples of illegal user queries are: "chocolate +-
	 * chip", "chocolate - - chip", and "chocolate chip -".
	 *
	 * this is copy paste from org.apache.solr.util.SolrPluginUtils as solr takes huge amount of libraries.
	 */
	private static CharSequence stripIllegalOperators(CharSequence s) {
		String temp = CONSECUTIVE_OP_PATTERN.matcher(s).replaceAll(" ");
		return DANGLING_OP_PATTERN.matcher(temp).replaceAll("");
	}

	/**
	 * Returns its input if there is an even (ie: balanced) number of
	 * '"' characters -- otherwise returns a String in which all '"'
	 * characters are striped out.
	 *
	 * this is copy paste from org.apache.solr.util.SolrPluginUtils as solr takes huge amount of libraries.
	 */
	private static CharSequence stripUnbalancedQuotes(CharSequence s) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '\"') {
				count++;
			}
		}
		if (0 == (count & 1)) {
			return s;
		}
		return s.toString().replace("\"", "");
	}
}
