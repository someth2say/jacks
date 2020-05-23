package org.redhattraining;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
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
import org.apache.commons.cli.Option;
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
            deepCauseToSysErr("Unhandled exception found", e);
            System.exit(-1);
        }
    }

    @Override
    public int run(final String... args) {
        CommandLine cmd;
        try {
            cmd = parseParams(args);
        } catch (final yqException e) {
            deepCauseToSysErr("Unable to parse parameters", e);
            return -1;
        }

        final OutputStream output = System.out;
        InputStream input = System.in;
        try {
            for (final Option option : cmd.getOptions()) {
                switch (option.getOpt()) {
                    case "f":
                        input = setInputFile(option.getValue());
                        break;
                    case "q":
                        input = applyQuery(input, option.getValue());
                        break;
                    case "o":
                        input = convertYamlToJson(input);
                }
            }
        } catch (final yqException e) {
            deepCauseToSysErr("Unable to process", e);
            return -1;
        }
        try {
            input.transferTo(output);
            output.flush();
        } catch (final IOException e) {
            deepCauseToSysErr("Error writting process results", e);
            return -1;
        }
        return 0;
    }

    private static void deepCauseToSysErr(final String headerMessage, final Throwable e) {
        System.err.println(headerMessage + ": " + e.getMessage());
        Throwable t = e.getCause();
        do {
            if (t != null) {
                System.err.println(t.getMessage());
                t = t.getCause();
            }
        } while (t != null && t != t.getCause());
    }

    private InputStream applyQuery(final InputStream file, final String query) throws yqException {
        try {
            return yamlPath(query, file);
        } catch (final JsonPathException e) {
            throw new yqException("JsonPath can not be parsed.", e);
        } catch (final Exception e) {
            throw new yqException(e);
        }
    }

    private InputStream setInputFile(final String fileName) throws yqException {
        final Path path = Paths.get(fileName);
        if (!path.toFile().exists()) {
            throw new yqException("File " + path.toAbsolutePath() + " does not exist");
        }
        try {
            return Files.newInputStream(path);
        } catch (final IOException e) {
            throw new yqException(e);
        }
    }

    private CommandLine parseParams(final String... args) throws yqException {
        CommandLine cmd;
        final Options options = new Options();
        options.addRequiredOption("f", "file", true, "The file to apply queries to.");
        options.addRequiredOption("q", "query", true, "The jsonPath query");
        options.addOption("o", "output", true, "The output format (json or yaml)");
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

    public static InputStream convertYamlToJson(final InputStream yaml) throws yqException {
        final ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj;
        try {
            obj = yamlReader.readValue(yaml, Object.class);
            return objectToJsonInputStream(obj);
        } catch (final JsonParseException e) {
            throw new yqException("Can not parse YAML", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not decode YAML", e);
        } catch (final IOException e) {
            throw new yqException("Can not read YAML", e);
        }
    }

    private static InputStream objectToJsonInputStream(final Object obj) throws yqException {
        final ObjectMapper jsonWriter = new ObjectMapper();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            jsonWriter.writeValue(out, obj);
        } catch (final JsonGenerationException e) {
            throw new yqException("Can not generate JSON", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not encode JSON", e);
        } catch (final IOException e) {
            throw new yqException("Can not write JSON", e);
        }
        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }

    private static InputStream objectToYamlInputStream(final Object obj) throws yqException {
        final ObjectMapper yamlWriter = new YAMLMapper();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            yamlWriter.writeValue(out, obj);
        } catch (final JsonGenerationException e) {
            throw new yqException("Can not generate YAML", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not encode YAML", e);
        } catch (final IOException e) {
            throw new yqException("Can not write YAML", e);
        }
        final InputStream in = new ByteArrayInputStream(out.toByteArray());
        return in;
    }

    public static InputStream convertJsonToYaml(final InputStream json) throws yqException {
        final ObjectMapper jsonReader = new ObjectMapper(new JsonFactory());
        Object obj;
        try {
            obj = jsonReader.readValue(json, Object.class);
            return objectToYamlInputStream(obj);
        } catch (final JsonParseException e) {
            throw new yqException("Can not parse JSON", e);
        } catch (final JsonMappingException e) {
            throw new yqException("Can not decode JSON", e);
        } catch (final IOException e) {
            throw new yqException("Can not read JSON", e);
        }
    }

    public static final InputStream jsonPath(final String jsonPath, final InputStream json) throws yqException {
        Object obj;
        try {
            obj = JsonPath.read(json, jsonPath);
            return objectToJsonInputStream(obj);
        } catch (final IOException e) {
            throw new yqException("Can not apply JsonPath",e);
        }
    }

    public static final InputStream yamlPath(final String yamlPath, final InputStream yaml) throws yqException {
        final InputStream json = convertYamlToJson(yaml);
        final InputStream jsonResult = jsonPath(yamlPath, json);
        final InputStream yamlResult = convertJsonToYaml(jsonResult);
        return yamlResult;
    }

    static class yqException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 1643405832912214952L;

        public yqException(final Throwable cause) {
            super(cause);
        }

        public yqException(final String message) {
            super(message);
        }

        public yqException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}