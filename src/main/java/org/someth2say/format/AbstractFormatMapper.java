package org.someth2say.format;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.someth2say.JacksException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AbstractFormatMapper<Mapper extends ObjectMapper> {
    protected final Mapper mapper;
    protected final Format format;


    public AbstractFormatMapper(final Mapper mapper, final Format format) {
        this.mapper = mapper;
        this.format = format;
    }

    public InputStream objectToInputStream(final Object obj) throws JacksException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            getObjectWriter().writeValue(out, obj);
        } catch (final JsonGenerationException e) {
            throw new JacksException("Can not generate " + format.name(), e);
        } catch (final JsonMappingException e) {
            throw new JacksException("Can not encode " + format.name(), e);
        } catch (final IOException e) {
            throw new JacksException("Can not write " + format.name(), e);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    protected ObjectWriter getObjectWriter() {
        return mapper.writerWithDefaultPrettyPrinter();
    }

    public final Object inputStreamToObject(final InputStream json) throws JacksException {
        return inputStreamToObject(json, Object.class);
    }

    public <T> T inputStreamToObject(final InputStream json, final Class<T> valueType) throws JacksException {
        try {
            return mapper.readValue(json, valueType);
        } catch (final JsonParseException e) {
            throw new JacksException("Can not parse " + format.name(), e);
        } catch (final JsonMappingException e) {
            throw new JacksException("Can not decode " + format.name(), e);
        } catch (final IOException e) {
            throw new JacksException("Can not read " + format.name(), e);
        }
    }

    public Object stringToObject(final String json) throws JacksException {
        return stringToObject(json, Object.class);
    }

    public <T> T stringToObject(final String json, final Class<T> valueType) throws JacksException {
        try {
            return mapper.readValue(json, valueType);
        } catch (final JsonParseException e) {
            throw new JacksException("Can not parse " + format.name(), e);
        } catch (final JsonMappingException e) {
            throw new JacksException("Can not decode " + format.name(), e);
        } catch (final IOException e) {
            throw new JacksException("Can not read " + format.name(), e);
        }
    }
}
