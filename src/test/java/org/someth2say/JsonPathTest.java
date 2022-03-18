package org.someth2say;

import org.junit.jupiter.api.Test;
import org.someth2say.formats.Yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPathTest {
    @Test
    public void simple_jsonpath() throws JacksException {
        final Yaml yaml = new Yaml();
        final Object yamlObject = yaml.stringToObject("""
                name: jacks
                version: 1.¡0
                """);
        final Object result = JsonPath.query("$.name", yamlObject);

        final String resultString = StreamUtils.convertStreamToString(yaml.objectToInputStream(result));

        assertEquals("\"jacks\"\n", resultString, ()->"JsonPath extraction not successful.");


    }

}