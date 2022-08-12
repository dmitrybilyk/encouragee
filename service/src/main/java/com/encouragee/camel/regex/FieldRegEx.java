package com.encouragee.camel.regex;

public class FieldRegEx {
	private String regex;
	private String replacement;
	private String description;

	public FieldRegEx() {}

	public FieldRegEx(String regex, String replacement) {
		this.regex = regex;
		this.replacement = replacement;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
