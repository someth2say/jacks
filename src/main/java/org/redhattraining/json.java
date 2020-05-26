package org.redhattraining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class json implements FormatMapper {
	private ObjectMapper jsonMapper = new JsonMapper();

	public json(ObjectMapper jsonMapper) {
		this.jsonMapper=jsonMapper;
	}

	public json(){
		this(new JsonMapper());
	}

	public final InputStream objectToInputStream(final Object obj) throws yqException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jsonMapper.writerWithDefaultPrettyPrinter().writeValue(out, obj);
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

	public final Object inputStreamToObject(final InputStream json) throws yqException {
		try {
			return jsonMapper.readValue(json, Object.class);
		} catch (JsonParseException e) {
			throw new yqException("Can not parse JSON", e);
		} catch (JsonMappingException e) {
			throw new yqException("Can not decode JSON", e);
		} catch (IOException e) {
			throw new yqException("Can not read JSON", e);
		}
	}

	@Override
	public Object stringToObject(String json) throws yqException {
		try {
			return jsonMapper.readValue(json, Object.class);
		} catch (JsonParseException e) {
			throw new yqException("Can not parse JSON", e);
		} catch (JsonMappingException e) {
			throw new yqException("Can not decode JSON", e);
		} catch (IOException e) {
			throw new yqException("Can not read JSON", e);
		}
	}

	public final Format getFormat(){
		return Format.JSON;
	}
}