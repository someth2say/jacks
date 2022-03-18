package org.someth2say.formats;

import java.util.function.Supplier;

public enum Format {
    JSON(Json::new, "json"),
    YAML(Yaml::new, "yml", "yaml"),
    XML(Xml::new, "xml"),
    PROPS(Properties::new, "props", "properties"),
    TXT(Txt::new, "txt");

    private final Supplier<FormatMapper> mapperProducer;
    private FormatMapper mapper;
    public final String[] extensions;

    Format(final Supplier<FormatMapper> mapperProducer, final String... extensions) {
        this.mapperProducer = mapperProducer;
        this.extensions = extensions;
    }

    public FormatMapper getMapper() {
        return mapper != null ? mapper : (mapper = mapperProducer.get());
    }
}