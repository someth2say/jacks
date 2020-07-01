package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.someth2say.formats.Json;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class jsonTest {

    @Test
    public void transformTest() throws yqException {
        Json json = new Json();
        String jsonString = "{\n  \"name\" : \"value\"\n}";
        InputStream is = new ByteArrayInputStream(jsonString.getBytes());
        Object jsonObject = json.inputStreamToObject(is);
        InputStream jsonIs = json.objectToInputStream(jsonObject);
        String newJsonString = yq.convertStreamToString(jsonIs);
        assertEquals(jsonString, newJsonString, ()->"Json does not transforms to stream correctly.");
    }

}