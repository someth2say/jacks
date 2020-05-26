package org.redhattraining;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class convert {
	private static final ObjectMapper jsonReader = new JsonMapper();
	private static final ObjectMapper yamlReader = new YAMLMapper();

	static yaml yaml = new yaml(yamlReader);
	static json json = new json(jsonReader);

	static final FormatMapper[] mappers = {json, yaml};

	public static final InputStream convertJsonToYaml(final InputStream json) throws yqException {
		Object obj;
		try {
			obj = jsonReader.readValue(json, Object.class);
			return yaml.objectToInputStream(obj);
		} catch (final JsonParseException e) {
			throw new yqException("Can not parse JSON", e);
		} catch (final JsonMappingException e) {
			throw new yqException("Can not decode JSON", e);
		} catch (final IOException e) {
			throw new yqException("Can not read JSON", e);
		}
	}

	public static final InputStream convertYamlToJson(final InputStream yaml) throws yqException {
		Object obj;
		try {
			obj = yamlReader.readValue(yaml, Object.class);
			return json.objectToInputStream(obj);
		} catch (final JsonParseException e) {
			throw new yqException("Can not parse YAML", e);
		} catch (final JsonMappingException e) {
			throw new yqException("Can not decode YAML", e);
		} catch (final IOException e) {
			throw new yqException("Can not read YAML", e);
		}
	}

}