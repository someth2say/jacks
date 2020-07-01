package org.someth2say;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class yq implements QuarkusApplication {
    @ConfigProperty(name = "project.name")
    String projectName;

    @ConfigProperty(name = "project.version")
    String projectVersion;

    static final Options options = new Options();
    static {
        options.addOption("f", "file", true, "The file to apply queries to.");
        options.addOption("q", "query", true, "The jsonPath query");
        options.addOption("i", "input", true, "Force the input format (disables autodetection)");
        options.addOption("o", "output", true, "The output format (json or yaml)");
        options.addOption("v", "version", false, "Displays the version and exits");
        options.addOption("h", "help", false, "Displays this help message and exits");

    }

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

        if (cmd.hasOption("v")) {
            System.out.println(projectVersion);
            return 0;
        }

        if (cmd.hasOption('h')){
            printHelp();
            return 0;
        }

        final OutputStream output = System.out;
        InputStream input;
        try {
            input = transform(cmd);
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

    static String convertStreamToString(final java.io.InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }

    private InputStream transform(final CommandLine cmd) throws yqException {
        Object currentObj = null;
        final InputStream input = prepareInput(cmd);
        Format initialFormat = null;
        final String rawContents = convertStreamToString(input);

        if (cmd.hasOption('i')) {
            final String optionValue = cmd.getOptionValue('i');
            try {
                initialFormat=Format.valueOf(optionValue.toUpperCase());
                currentObj = initialFormat.mapper.stringToObject(rawContents);
            } catch (IllegalArgumentException e) {
                throw new yqException("Illegal format: "+optionValue, e);
            }
        } else {
            for (Format format : Format.values()){
                try {
                    currentObj = format.mapper.stringToObject(rawContents);
                    initialFormat = format;
                    break;
                } catch (final yqException e) {
                    // ignore
                }
            }
        }

        if (currentObj == null) {
            throw new yqException("Unable to infer input format");
        }

        currentObj = applyQueries(cmd, currentObj);

        return transformOutput(cmd, currentObj, input, initialFormat);

    }

    private Object applyQueries(final CommandLine cmd, Object currentObj) throws yqException {
        if (cmd.getOptionValues('q') != null) {
            for (final String query : cmd.getOptionValues('q')) {
                currentObj = JsonPath.query(query, currentObj);
            }
        }
        return currentObj;
    }

    private InputStream transformOutput(final CommandLine cmd, final Object currentObj, InputStream input,
            final Format initialFormat) throws yqException {

                
        Format targetFormat = initialFormat;

        if (cmd.hasOption('o')) {
            final String optionValue = cmd.getOptionValue('o');
            try {
                targetFormat=Format.valueOf(optionValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new yqException("Illegal format: "+optionValue, e);
            }
        }

        input = targetFormat.mapper.objectToInputStream(currentObj);
        return input;
    }

    private InputStream prepareInput(final CommandLine cmd) throws yqException {
        InputStream input;
        if (cmd.hasOption('f')) {
            input = setInput(cmd.getOptionValue('f'));
        } else {
            input = System.in;
        }
        return input;
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

    private InputStream setInput(final String fileName) throws yqException {
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

        try {
            final CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            printHelp();
            throw new yqException("Cannot parse parameters", e);
        }
        
        if (cmd.hasOption('f') && cmd.getOptionValues("f").length > 1) {
            printHelp();
            throw new yqException("At most a single f(file) parameter is allowed.");
        }

        if (cmd.hasOption('o') && cmd.getOptionValues("o").length > 1) {
            printHelp();
            throw new yqException("At most a single o(utput) parameter is allowed.");
        }

        return cmd;
    }

    private void printHelp() {
        new HelpFormatter().printHelp(projectName
                + " [-f FILENAME] [-q QUERY]* [-o OUTPUT]",
         "Reads JSON and YAML files and apply JsonPath queries to them.", 
         options, 
         "If no filename provided, standard input is used (^D to terminate).\n"
                + "If no output is provided, the output format is kept.");
    }
}