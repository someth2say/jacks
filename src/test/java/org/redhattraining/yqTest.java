package org.redhattraining;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import net.minidev.json.JSONObject;

@QuarkusTest
public class yqTest {
    
    @Test
    public void yamlToJsonToYaml() throws JsonParseException, JsonMappingException, IOException {
        final String json = yq.convertYamlToJson(new File("src/test/resources/dco.yml"));
        final String yaml = yq.convertJsonToYaml(json);
        System.out.println(json);
        System.out.println(yaml);
    }

    @Test
    public void yamlPath() throws IOException {
        final String jsonPath="$.chapters[*].chapter_word";
        final String yaml = Files.readString(Paths.get("src/test/resources/dco.yml"));
		final String chapters = yq.yamlPath(jsonPath, yaml);
        System.out.println(chapters);
    }


    @Test
    public void jsonPath() throws IOException{
        
        final String jsonPath="$.chapters[*].chapter_word";
        final String  json = yq.convertYamlToJson(new File("src/test/resources/dco.yml"));

		final String chapters = yq.jsonPath(jsonPath, json);
        System.out.println(chapters);
    }


}