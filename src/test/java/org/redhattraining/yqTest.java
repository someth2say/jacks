package org.redhattraining;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yqTest {
    
    @Test
    public void runTest(){
        yq.main("-f","src/test/resources/dco.yml","-q","$.chapters[*].chapter_word");
    }

    @Test
    public void runMissingFile(){
        yq.main("-f","src/test/resources/missing.yml","-q","$.chapters[*].chapter_word");
    }

    @Test
    public void runInvalidFile(){
        yq.main("-f","src/main/resources/application.properties","-q","$.chapters[*].chapter_word");
    }


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