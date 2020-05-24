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
}