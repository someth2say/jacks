package org.redhattraining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.jayway.jsonpath.JsonPathException;

public class yaml {

    static final InputStream objectToYamlInputStream(final Object obj) throws yqException {
        final ObjectMapper yamlWriter = new YAMLMapper();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            yamlWriter.writeValue(out, obj);
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

    public static final InputStream yamlPath(final String yamlPath, final InputStream yaml) throws yqException {
        try {
            final InputStream json = convert.convertYamlToJson(yaml);
            final InputStream jsonResult = org.redhattraining.json.jsonPath(yamlPath, json);
            final InputStream yamlResult = convert.convertJsonToYaml(jsonResult);
            return yamlResult;
        } catch (final JsonPathException e) {
            throw new yqException("JsonPath can not be parsed.", e);
        } catch (final Exception e) {
            throw new yqException(e);
        }

    }

}