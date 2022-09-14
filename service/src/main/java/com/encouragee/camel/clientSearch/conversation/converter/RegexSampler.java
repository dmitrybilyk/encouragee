package com.encouragee.camel.clientSearch.conversation.converter;

class RegexSampler {

	private static final String HEX_DIGITS = "0123456789abcdef";

	/**
	 * Creates a list of regexps, so that for any 0-padded hexadecimal number
	 * between 0 (included) and the specified parameter (excluded)
	 * there's always at least one regexp that matches this number.
	 * <p>
	 * Patterns are created for sampling (like modulo)
	 * and should be used to match the ending of a longer hexadecimal number.
	 *
	 * @param maxHex the maximum number to match (excluded); must be 0-padded to match length
	 * @return list of patterns to use
	 */
	public String createHexRegexp(String maxHex) {
		// create 1 regexp per digit, allowing all values up to that digit (excluded)
		StringBuilder regexp = new StringBuilder();

		for (int i = maxHex.length() - 1; i >= 0; i--) {
			int digitValue = Character.getNumericValue(maxHex.charAt(i));
			if (digitValue == 0) {
				continue; // current digit 0 is excluded -> there are no allowed values
			}
			if (regexp.length() > 0) {
				regexp.append("|");
			}
			regexp.append(maxHex, 0, i);
			appendMatchSingleDigitUpTo(regexp, digitValue);
			appendMatchNumberOfAnyDigits(regexp, maxHex.length() - i - 1);
		}

		return regexp.toString();
	}

	private void appendMatchSingleDigitUpTo(StringBuilder regexp, int maxExcluded) {
		// return simplified patterns if possible - for easier debugging
		if (maxExcluded == 1) {
			regexp.append("0");
		} else if (maxExcluded == 2) {
			regexp.append("[01]");
		} else if (maxExcluded <= 10) {
			regexp.append("[0-").append(HEX_DIGITS.charAt(maxExcluded - 1)).append("]");
		} else if (maxExcluded == 11) {
			regexp.append("[0-9a]");
		} else if (maxExcluded == 12) {
			regexp.append("[0-9ab]");
		} else {
			regexp.append("[0-9a-").append(HEX_DIGITS.charAt(maxExcluded - 1)).append("]");
		}
	}

	private void appendMatchNumberOfAnyDigits(StringBuilder regexp, int digits) {
		// return simplified patterns if possible - for easier debugging
		if (digits == 1) {
			regexp.append("[0-9a-f]");
		} else if (digits > 1) {
			regexp.append("[0-9a-f]").append("{").append(digits).append("}");
		}
	}
}
