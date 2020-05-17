//usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS io.quarkus:quarkus-arc:1.4.2.Final
package org.redhattraining;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.jayway.jsonpath.JsonPath;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import picocli.CommandLine;
import picocli.CommandLine.Command;
// import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@QuarkusMain
@Command(description = "Queries yaml files using jsonPath format.",
         name = "yq", mixinStandardHelpOptions = true, version = "yq 1.0.0-SNAPSHO")
public class yq implements QuarkusApplication, Callable<Integer> {

    @Parameters(index = "0", description = "The file to apply queries to.")
    private File file;

    @Parameters(index = "1", description = "The jsonPath query")
    private String query;
    // @Option(names = {"-a", "--algorithm"}, description = "MD5, SHA-1, SHA-256, ...")
    // private String algorithm = "SHA-1";

    public static void main(final String... args) {
        Quarkus.run(yq.class, args);        
    }

    @Override
    public Integer call() throws Exception {
        final String fileName = file.getName();
        System.out.println(fileName);
        if (file.exists()) {
            // if (fileName.endsWith(".yml")||fileName.endsWith(".yaml")){
            //     final String json = convertYamlToJson(file);
            //     System.out.println(json);
            // } else if (fileName.endsWith(".json")) {
            //     final String yaml = convertJsonToYaml(file);
            //     System.out.println(yaml);
            // }
            String result = yamlPath(query, file);
            System.out.println(result);
            return 0;
        } else {
            System.err.println("File "+fileName+" does not exist.");
            return -1;
        }
     }

    @Override
    public int run(final String... args) throws Exception {   
        return new CommandLine(new yq()).execute(args);
    }

    public static String convertYamlToJson(final String yaml) throws JsonMappingException, JsonProcessingException {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        final Object obj = yamlReader.readValue(yaml, Object.class);
    
        final ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    public static void convertYamlToJson(final File yamlFile, final File jsonFile) throws JsonParseException, JsonMappingException, IOException  {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        final Object obj = yamlReader.readValue(yamlFile, Object.class);
    
        final ObjectMapper jsonWriter = new ObjectMapper();
        jsonWriter.writeValue(jsonFile, obj);
    }
    
    public static String convertYamlToJson(final File yamlFile) throws JsonParseException, JsonMappingException, IOException  {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        final Object obj = yamlReader.readValue(yamlFile, Object.class);
    
        final ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }
    

    public static String convertJsonToYaml(final String json) throws JsonMappingException, JsonProcessingException {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        final Object obj = jsonReader.readValue(json, Object.class);
    
        final ObjectMapper yamlWriter = new YAMLMapper();
        return yamlWriter.writeValueAsString(obj);
    }


    public static void convertJsonToYaml(final File jsonFile, final File yamlFile) throws JsonParseException, JsonMappingException, IOException  {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        final Object obj = jsonReader.readValue(jsonFile, Object.class);
    
        final ObjectMapper yamlWriter = new ObjectMapper();
        yamlWriter.writeValue(yamlFile, obj);
    }

    public static String convertJsonToYaml(final File jsonFile) throws JsonParseException, JsonMappingException, IOException  {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        final Object obj = jsonReader.readValue(jsonFile, Object.class);
    
        final ObjectMapper yamlWriter = new ObjectMapper();
        return yamlWriter.writeValueAsString(obj);
    }

    public static final String jsonPath(final String jsonPath, final String json){
        Object read = JsonPath.read(json, jsonPath);
        return read.toString();
    }

    public static final String yamlPath(final String yamlPath, final String yaml) throws JsonMappingException, JsonProcessingException{
        String json = convertYamlToJson(yaml);
        String jsonResult = jsonPath(yamlPath, json);
        String yamlResult = convertJsonToYaml(jsonResult);
        return yamlResult;
    }

    public static final String yamlPath(final String yamlPath, final File yaml) throws IOException{
        String json = convertYamlToJson(yaml);
        String jsonResult = jsonPath(yamlPath, json);
        String yamlResult = convertJsonToYaml(jsonResult);
        return yamlResult;
    }
}