package org.redhattraining;

import static org.redhattraining.convert.convertJsonToYaml;
import static org.redhattraining.convert.convertYamlToJson;

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
        final InputStream json = convertYamlToJson(Files.newInputStream(Paths.get("src/test/resources/dco.yml")));
        final InputStream yaml = convertJsonToYaml(json);
        yaml.transferTo(System.out);
        System.out.flush();
    }

}