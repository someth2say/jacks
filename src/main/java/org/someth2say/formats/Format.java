package org.someth2say.formats;

public enum Format {
    JSON(new Json(), "json"),
    YAML(new Yaml(), "yml", "yaml"),
    XML(new Xml(), "xml"),
    PROPS(new Properties(), "props", "properties"),
    TXT(new Txt(), "txt");

    public final FormatMapper mapper;
    public final String[] extensions;

    Format(final FormatMapper mapper, final String... extensions) {
        this.mapper = mapper;
        this.extensions = extensions;
    }

}