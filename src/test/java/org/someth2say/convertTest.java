package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.someth2say.convert.convertJsonToYaml;
import static org.someth2say.convert.convertYamlToJson;
import static org.someth2say.convert.json;
import static org.someth2say.convert.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class convertTest {

    @Test
    public void yamlToJsonToYaml() throws IOException, yqException {
        final Object yamlObj = yaml
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.yaml")));
        final InputStream yamlIs = yaml.objectToInputStream(yamlObj);
        final InputStream jsonIs = convertYamlToJson(yamlIs);
        Object jsonObj = json.inputStreamToObject(jsonIs);
        assertEquals(yamlObj, jsonObj, () -> "Transformed objects are not equals.");
    }


    @Test
    public void yamlTomlToJson() throws IOException, yqException {
        final Object jsonObj = json
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.json")));
        final InputStream jsonIs = json.objectToInputStream(jsonObj);
        final InputStream yamlIs = convertJsonToYaml(jsonIs);
        Object yamlObj = yaml.inputStreamToObject(yamlIs);
        assertEquals(jsonObj, yamlObj, () -> "Transformed objects are not equals.");
    }
}