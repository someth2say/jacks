package org.someth2say;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class convert {
	private static final JsonMapper jsonMapper = new JsonMapper();
	private static final YAMLMapper yamlMapper = new YAMLMapper();

	static Yaml yaml = new Yaml(yamlMapper);
	static Json json = new Json(jsonMapper);
	static Plain plain = new Plain();

	static final FormatMapper[] mappers = {json, yaml};

	public static final InputStream convertJsonToYaml(final InputStream json) throws yqException {
		Object obj;
		try {
			obj = jsonMapper.readValue(json, Object.class);
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
			obj = yamlMapper.readValue(yaml, Object.class);
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