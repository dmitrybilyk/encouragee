package com.encouragee.camel.regex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public final class RegExUtils {
	private static final Logger log = LoggerFactory.getLogger(RegExUtils.class);

	private RegExUtils() {}

	/**
	 * Apply regexes to object. Results are stored in the same object
	 *
	 * @param regExMap regular expressions to apply. The keys of the map are expected to be field names
	 * @param object   the object whose field contents will be replaced
	 */
	public static void applyRegexesToObject(Map<String, List<FieldRegEx>> regExMap, Object object) {
		if (regExMap == null || regExMap.isEmpty()) {
			log.error("The list of regular expressions is null or empty");
			return;
		}
		if (object == null) {
			log.error("Cannot apply regular expressions to a null field");
			return;
		}

		Stream.of(object.getClass().getDeclaredFields())
				.filter(field -> String.class.equals(field.getType()))
				.forEach(field -> applyRegexToField(regExMap, object, field));
	}

	private static void applyRegexToField(Map<String, List<FieldRegEx>> regExMap, Object object, Field field) {
		regExMap.getOrDefault(field.getName(), emptyList()).forEach(fieldRegEx -> {
			try {
				field.setAccessible(true);
				String originalValue = (String) field.get(object);
				if (originalValue != null) {
					String applied = originalValue.replaceAll(fieldRegEx.getRegex(), fieldRegEx.getReplacement());
					field.set(object, applied);
				}
			} catch (IllegalAccessException exception) {
				log.error("Error when applying the regex {} with the replacement {} for the field {}",
						fieldRegEx.getRegex(), fieldRegEx.getReplacement(), field.getName(), exception);
			}
		});
	}
}
