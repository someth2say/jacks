package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.someth2say.ConvertUtils.convertJsonToYaml;
import static org.someth2say.ConvertUtils.convertYamlToJson;
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
        final Object yamlObj = Format.YAML.getMapper()
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.yaml")));
        final InputStream yamlIs = Format.YAML.getMapper().objectToInputStream(yamlObj);
        final InputStream jsonIs = convertYamlToJson(yamlIs);
        Object jsonObj = Format.JSON.getMapper().inputStreamToObject(jsonIs);
        assertEquals(yamlObj, jsonObj, () -> "Transformed objects are not equals.");
    }


    @Test
    public void yamlTomlToJson() throws IOException, JacksException {
        final Object jsonObj = Format.JSON.getMapper()
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.json")));
        final InputStream jsonIs = Format.JSON.getMapper().objectToInputStream(jsonObj);
        final InputStream yamlIs = convertJsonToYaml(jsonIs);
        Object yamlObj = Format.YAML.getMapper().inputStreamToObject(yamlIs);
        assertEquals(jsonObj, yamlObj, () -> "Transformed objects are not equals.");
    }
}