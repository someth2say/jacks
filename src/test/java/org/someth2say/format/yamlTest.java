package org.someth2say.format;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.someth2say.JacksException;
import org.someth2say.StreamUtils;
import org.someth2say.format.Yaml;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yamlTest {
    @Test
    public void transformTest() throws JacksException {
        Yaml yaml = new Yaml();
        String yamlString = "name: \"value\"\n";
        InputStream is = new ByteArrayInputStream(yamlString.getBytes());
        Object yamlObject = yaml.inputStreamToObject(is);
        InputStream yamlIs = yaml.objectToInputStream(yamlObject);
        String newJsonString = StreamUtils.convertStreamToString(yamlIs);
        assertEquals(yamlString, newJsonString, ()->"Yaml does not transforms to stream correctly.");
    }
}