package org.redhattraining;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.Test;
import org.redhattraining.yq.yqException;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yqTest {

    @Test
    public void runTest() {
        int exitcode = new yq().run("-f", "src/test/resources/dco.yml", "-q", "$.chapters[*].chapter_word");
        assertEquals(0, exitcode, () -> "Basic run failed.");
    }

    @Test
    public void runMissingFile() {
        new yq().run("-f", "src/test/resources/missing.yml", "-q", "$.chapters[*].chapter_word");
    }

    @Test
    public void runInvalidFile() {
        new yq().run("-f", "src/main/resources/application.properties", "-q", "$.chapters[*].chapter_word");
    }

    @Test
    public void invalidJsonPath() {
        new yq().run("-f", "src/main/resources/application.properties", "-q",
                "$.chapters[?is_compreview = true].chapter_word");
    }

    @Test
    public void yamlToJsonToYaml() throws yqException, IOException {
        final InputStream json = yq.convertYamlToJson(Files.newInputStream(Paths.get("src/test/resources/dco.yml")));
        final InputStream yaml = yq.convertJsonToYaml(json);
        yaml.transferTo(System.out);
        System.out.flush();
    }

    @Test
    public void yamlPath() throws IOException, yqException {
        final String jsonPath="$.chapters[*].chapter_word";
        final InputStream yaml = Files.newInputStream(Paths.get("src/test/resources/dco.yml"));
		final InputStream chapters = yq.yamlPath(jsonPath, yaml);
        chapters.transferTo(System.out);
        System.out.flush();
    }

    @Test
    public void jsonPath() throws IOException, yqException {
        
        final String jsonPath="$.chapters[*].chapter_word";
        final InputStream json = yq.convertYamlToJson(Files.newInputStream(Paths.get("src/test/resources/dco.yml")));

		final InputStream chapters = yq.jsonPath(jsonPath, json);
        chapters.transferTo(System.out);
        System.out.flush();
    }


}