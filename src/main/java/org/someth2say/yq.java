package org.someth2say;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.someth2say.formats.Format;

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

        if (cmd.hasOption('h')) {
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

    private static class TransformationStatus {
        Format initialFormat = null;
        Object currentObj = null;
    }

    private InputStream transform(final CommandLine cmd) throws yqException {
        final InputStream input = prepareInput(cmd);

        final String rawContents = convertStreamToString(input);

        TransformationStatus status = computeInitialStatus(cmd, rawContents);

        status.currentObj = applyQueries(cmd, status.currentObj);

        return transformOutput(cmd, status);

    }

    private TransformationStatus computeInitialStatus(final CommandLine cmd, final String rawContents)
            throws yqException {
        TransformationStatus status = new TransformationStatus();

        status.initialFormat = inferFormatFromCommandLine(cmd);

        if (status.initialFormat == null) {
            status.initialFormat = inferFormatFromFilename(cmd);
        }

        if (status.initialFormat == null) {
            status.initialFormat = inferFormatFromContent(rawContents, status);
        }

        if (status.initialFormat != null) {
            if (status.currentObj == null) {
                status.currentObj = status.initialFormat.mapper.stringToObject(rawContents);
            }
        } else {
            throw new yqException("Unable to infer input format");
        }

        return status;
    }

    private Format inferFormatFromCommandLine(final CommandLine cmd) throws yqException {
        if (cmd.hasOption('i')) {
            final String optionValue = cmd.getOptionValue('i');
            try {
                return Format.valueOf(optionValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new yqException("Illegal format: " + optionValue, e);
            }
        }
        return null;
    }

    private Format inferFormatFromFilename(CommandLine cmd) {
        if (cmd.hasOption("f")) {
            String extension = getExtension(cmd.getOptionValue("f"));
            for (Format format : Format.values()) {
                for (String formatExtension : format.extensions) {
                    if (formatExtension.equalsIgnoreCase(extension)) {
                        return format;
                    }
                }
            }
        }
        return null;
    }

    private String getExtension(String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse("");
    }

    private Format inferFormatFromContent(final String rawContents, TransformationStatus status) {
        for (Format format : Format.values()) {
            try {
                status.currentObj = format.mapper.stringToObject(rawContents);
                status.initialFormat = format;
                return format;
            } catch (final yqException e) {
                // ignore
            }
        }
        return null;
    }

    private Object applyQueries(final CommandLine cmd, Object currentObj) throws yqException {
        if (cmd.getOptionValues('q') != null) {
            for (final String query : cmd.getOptionValues('q')) {
                currentObj = JsonPath.query(query, currentObj);
            }
        }
        return currentObj;
    }

    private InputStream transformOutput(final CommandLine cmd, final TransformationStatus status) throws yqException {

        Format targetFormat = status.initialFormat;

        if (cmd.hasOption('o')) {
            final String optionValue = cmd.getOptionValue('o');
            try {
                targetFormat = Format.valueOf(optionValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new yqException("Illegal format: " + optionValue, e);
            }
        }

        return targetFormat.mapper.objectToInputStream(status.currentObj);
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
        new HelpFormatter().printHelp(projectName + " [-f FILENAME] [-q QUERY]* [-o OUTPUT]",
                "Reads JSON and YAML files and apply JsonPath queries to them.", options,
                "If no filename provided, standard input is used (^D to terminate).\n"
                        + "If no output is provided, the output format is kept.");
    }
}