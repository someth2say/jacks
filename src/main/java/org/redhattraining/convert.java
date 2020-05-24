package org.redhattraining;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class convert {

	public static final InputStream convertJsonToYaml(final InputStream json) throws yqException {
	    final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
	    Object obj;
	    try {
	        obj = jsonReader.readValue(json, Object.class);
	        return yaml.objectToYamlInputStream(obj);
	    } catch (final JsonParseException e) {
	        throw new yqException("Can not parse JSON", e);
	    } catch (final JsonMappingException e) {
	        throw new yqException("Can not decode JSON", e);
	    } catch (final IOException e) {
	        throw new yqException("Can not read JSON", e);
	    }
	}

	public static final InputStream convertYamlToJson(final InputStream yaml) throws yqException {
	    final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
	    Object obj;
	    try {
	        obj = yamlReader.readValue(yaml, Object.class);
	        return json.objectToJsonInputStream(obj);
	    } catch (final JsonParseException e) {
	        throw new yqException("Can not parse YAML", e);
	    } catch (final JsonMappingException e) {
	        throw new yqException("Can not decode YAML", e);
	    } catch (final IOException e) {
	        throw new yqException("Can not read YAML", e);
	    }
	}
    
}