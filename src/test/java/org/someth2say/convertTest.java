package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.someth2say.convert.convertJsonToYaml;
import static org.someth2say.convert.convertYamlToJson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.someth2say.formats.Format;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class convertTest {

    @Test
    public void yamlToJsonToYaml() throws IOException, JacksException {
        final Object yamlObj = Format.YAML.mapper
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.yaml")));
        final InputStream yamlIs = Format.YAML.mapper.objectToInputStream(yamlObj);
        final InputStream jsonIs = convertYamlToJson(yamlIs);
        Object jsonObj = Format.JSON.mapper.inputStreamToObject(jsonIs);
        assertEquals(yamlObj, jsonObj, () -> "Transformed objects are not equals.");
    }


    @Test
    public void yamlTomlToJson() throws IOException, JacksException {
        final Object jsonObj = Format.JSON.mapper
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.json")));
        final InputStream jsonIs = Format.JSON.mapper.objectToInputStream(jsonObj);
        final InputStream yamlIs = convertJsonToYaml(jsonIs);
        Object yamlObj = Format.YAML.mapper.inputStreamToObject(yamlIs);
        assertEquals(jsonObj, yamlObj, () -> "Transformed objects are not equals.");
    }
}