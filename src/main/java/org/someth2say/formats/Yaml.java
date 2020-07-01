package org.someth2say.formats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import org.someth2say.yqException;

public class Yaml implements FormatMapper {
    public YAMLMapper yamlMapper;

    public Yaml(YAMLMapper yamlMapper) {
        this.yamlMapper=yamlMapper;
	}

    public Yaml(){
        this(new YAMLMapper());
    }
    
	public final InputStream objectToInputStream(final Object obj) throws yqException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            yamlMapper.writerWithDefaultPrettyPrinter().writeValue(out, obj);
        } catch (final JsonGenerationException e) {
            throw new yqException("Can not generate YAML", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not encode YAML", e);
        } catch (final IOException e) {
            throw new yqException("Can not write YAML", e);
        }
        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }

    public final Object inputStreamToObject(final InputStream json) throws yqException {
        Class<Object> valueType = Object.class;
        return inputStreamToObject(json, valueType);
    }

    @Override
    public <T> T inputStreamToObject(final InputStream json, Class<T> valueType) throws yqException {
        try {
            return yamlMapper.readValue(json, valueType);
        } catch (JsonParseException e) {
            throw new yqException("Can not parse YAML", e);
        } catch (JsonMappingException e) {
            throw new yqException("Can not decode YAML", e);
        } catch (IOException e) {
            throw new yqException("Can not read YAML", e);
        }
    }

    @Override
    public Object stringToObject(String yaml) throws yqException {
        Class<Object> valueType = Object.class;
        return stringToObject(yaml, valueType);
    }

    @Override
    public <T> T stringToObject(String yaml, Class<T> valueType) throws yqException {
        try {
            return yamlMapper.readValue(yaml, valueType);
        } catch (JsonParseException e) {
            throw new yqException("Can not parse YAML", e);
        } catch (JsonMappingException e) {
            throw new yqException("Can not decode YAML", e);
        } catch (IOException e) {
            throw new yqException("Can not read YAML", e);
        }
    }

}