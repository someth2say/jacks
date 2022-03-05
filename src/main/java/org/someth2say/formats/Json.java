package org.someth2say.formats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.someth2say.JacksException;

public class Json implements FormatMapper {
	public JsonMapper jsonMapper = new JsonMapper();


	public Json(JsonMapper jsonMapper) {
		this.jsonMapper=jsonMapper;
	}

	public Json(){
		this(new JsonMapper());
	}

	public final InputStream objectToInputStream(final Object obj) throws JacksException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			jsonMapper.writerWithDefaultPrettyPrinter().writeValue(out, obj);
		} catch (final JsonGenerationException e) {
			throw new JacksException("Can not generate JSON", e);
		} catch (final JsonMappingException e) {
			throw new JacksException("Can not encode JSON", e);
		} catch (final IOException e) {
			throw new JacksException("Can not write JSON", e);
		}
		final InputStream in = new ByteArrayInputStream(out.toByteArray());
		return in;
	}

	@Override
	public final Object inputStreamToObject(final InputStream json) throws JacksException {
		Class<Object> valueType = Object.class;
		return inputStreamToObject(json, valueType);
	}

	@Override
	public <T> T inputStreamToObject(final InputStream json, Class<T> valueType) throws JacksException {
		try {
			return jsonMapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			throw new JacksException("Can not parse JSON", e);
		} catch (JsonMappingException e) {
			throw new JacksException("Can not decode JSON", e);
		} catch (IOException e) {
			throw new JacksException("Can not read JSON", e);
		}
	}

	@Override
	public Object stringToObject(String json) throws JacksException {
		return stringToObject(json, Object.class);
	}

	public <T> T stringToObject(String json, Class<T> valueType) throws JacksException {
		try {
			return jsonMapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			throw new JacksException("Can not parse JSON", e);
		} catch (JsonMappingException e) {
			throw new JacksException("Can not decode JSON", e);
		} catch (IOException e) {
			throw new JacksException("Can not read JSON", e);
		}
	}
}