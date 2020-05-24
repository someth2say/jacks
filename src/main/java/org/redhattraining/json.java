package org.redhattraining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class json {

	static final InputStream objectToJsonInputStream(final Object obj) throws yqException {
	    final ObjectMapper jsonWriter = new ObjectMapper();
	    final ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
	        jsonWriter.writeValue(out, obj);
	    } catch (final JsonGenerationException e) {
	        throw new yqException("Can not generate JSON", e);
	    } catch (final JsonMappingException e) {
	        throw new yqException("Can not encode JSON", e);
	    } catch (final IOException e) {
	        throw new yqException("Can not write JSON", e);
	    }
	    final InputStream in = new ByteArrayInputStream(out.toByteArray());
	    return in;
	}

	public static final InputStream jsonPath(final String jsonPath, final InputStream json) throws yqException {
	    Object obj;
	    try {
	        obj = JsonPath.read(json, jsonPath);
	        return objectToJsonInputStream(obj);
	    } catch (final IOException e) {
	        throw new yqException("Can not apply JsonPath",e);
	    }
	}
    
}