package com.encouragee.camel.regex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldRegexMapper {
	private static final Logger log = LoggerFactory.getLogger(FieldRegexMapper.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	public Map<String, List<FieldRegEx>> regExFileParser(String path) {
		if (path == null) {
			log.warn("The path of the regex file is null");
			return Collections.emptyMap();
		}

		try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
			return objectMapper.readValue(
					inputStream,
					new TypeReference<HashMap<String, List<FieldRegEx>>>(){});
		} catch (Exception exception) {
			log.warn("Error when parsing the regular expressions from the file {}", path, exception);
			return Collections.emptyMap();
		}
	}
}
