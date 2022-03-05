package org.someth2say.formats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import org.someth2say.JacksException;

public class Properties implements FormatMapper {
	public JavaPropsMapper propsMapper = new JavaPropsMapper();

	public Properties(JavaPropsMapper propsMapper) {
		this.propsMapper=propsMapper;
	}

	public Properties(){
		this(new JavaPropsMapper());
	}

	public final InputStream objectToInputStream(final Object obj) throws JacksException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			propsMapper.writerWithDefaultPrettyPrinter().writeValue(out, obj);
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
		return inputStreamToObject(json, Object.class);
	}

	@Override
	public <T> T inputStreamToObject(final InputStream json, Class<T> valueType) throws JacksException {
		try {
			return propsMapper.readValue(json, valueType);
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
			return propsMapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			throw new JacksException("Can not parse JSON", e);
		} catch (JsonMappingException e) {
			throw new JacksException("Can not decode JSON", e);
		} catch (IOException e) {
			throw new JacksException("Can not read JSON", e);
		}
	}
}