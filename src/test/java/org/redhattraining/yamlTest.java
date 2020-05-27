package org.redhattraining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yamlTest {
    @Test
    public void transformTest() throws yqException {
        Yaml yaml = new Yaml();
        String yamlString = "---\nname: \"value\"\n";
        InputStream is = new ByteArrayInputStream(yamlString.getBytes());
        Object yamlObject = yaml.inputStreamToObject(is);
        InputStream yamlIs = yaml.objectToInputStream(yamlObject);
        String newJsonString = yq.convertStreamToString(yamlIs);
        assertEquals(yamlString, newJsonString, ()->"Json does not transforms to stream correctly.");
    }
}