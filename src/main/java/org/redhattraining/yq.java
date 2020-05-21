package org.redhattraining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class yq implements QuarkusApplication {
    public static void main(final String... args) {
        try {
            Quarkus.run(yq.class, args);
        } catch (final Exception e) {
            System.err.println("Unhandled exception found: " + e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    @Override
    public int run(final String... args) {
        CommandLine cmd;
        try {
            cmd = parseParams(args);
        } catch (final yqException e) {
            return -1;
        }

        final String fileName = cmd.getOptionValue('f');
        final File file = Paths.get(fileName).toFile();
        if (file.exists()) {
            final String query = cmd.getOptionValue('q');
            try {
                final String result = yamlPath(query, file);
                System.out.println(result);
                return 0;
            } catch (final JsonPathException e) {
                System.err.println("File " + fileName + " can not be parsed.");
                System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
                return -1;
            } catch (final IOException e) {
                System.err.println("File " + fileName + " can not be read.");
                System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
                return -1;
            } catch (final Exception e) {
                System.err.println("Unhandled exception found");
                System.err.println(e.getClass().getName() + ": " + e.getLocalizedMessage());
                return -1;
            }
        } else {
            System.err.println("File " + fileName + " does not exist.");
            return -1;
        }
    }

    private CommandLine parseParams(final String... args) throws yqException {
        CommandLine cmd;
        final Options options = new Options();
        options.addRequiredOption("f", "file", true, "The file to apply queries to.");
        options.addRequiredOption("q", "query", true, "The jsonPath query");
        try {
            final CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(args[0], options);
            throw new yqException(e);
        }
        return cmd;
    }

    public static String convertYamlToJson(final String yaml) throws JsonMappingException, JsonProcessingException {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        final Object obj = yamlReader.readValue(yaml, Object.class);

        final ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    public static void convertYamlToJson(final File yamlFile, final File jsonFile)
            throws JsonParseException, JsonMappingException, IOException {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        final Object obj = yamlReader.readValue(yamlFile, Object.class);

        final ObjectMapper jsonWriter = new ObjectMapper();
        jsonWriter.writeValue(jsonFile, obj);
    }

    public static String convertYamlToJson(final File yamlFile)
            throws JsonParseException, JsonMappingException, IOException {
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

    public static void convertJsonToYaml(final File jsonFile, final File yamlFile)
            throws JsonParseException, JsonMappingException, IOException {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        final Object obj = jsonReader.readValue(jsonFile, Object.class);

        final ObjectMapper yamlWriter = new ObjectMapper();
        yamlWriter.writeValue(yamlFile, obj);
    }

    public static String convertJsonToYaml(final File jsonFile)
            throws JsonParseException, JsonMappingException, IOException {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        final Object obj = jsonReader.readValue(jsonFile, Object.class);

        final ObjectMapper yamlWriter = new ObjectMapper();
        return yamlWriter.writeValueAsString(obj);
    }

    public static final String jsonPath(final String jsonPath, final String json) {
        final Object read = JsonPath.read(json, jsonPath);
        return read.toString();
    }

    public static final String yamlPath(final String yamlPath, final String yaml)
            throws JsonMappingException, JsonProcessingException {
        final String json = convertYamlToJson(yaml);
        final String jsonResult = jsonPath(yamlPath, json);
        final String yamlResult = convertJsonToYaml(jsonResult);
        return yamlResult;
    }

    public static final String yamlPath(final String yamlPath, final File yaml)
            throws JsonParseException, JsonMappingException, IOException {
        final String json = convertYamlToJson(yaml);
        final String jsonResult = jsonPath(yamlPath, json);
        final String yamlResult = convertJsonToYaml(jsonResult);
        return yamlResult;
    }

    class yqException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 1643405832912214952L;

        public yqException(Throwable cause){
            super(cause);
        }
    }
}