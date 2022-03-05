package org.someth2say.formats;

public enum Format {
    JSON(new Json(),"json"), PROPS(new Properties(),"props","properties"), YAML(new Yaml(),"yml","yaml"),  TXT(new Txt(),"txt");

    public final FormatMapper mapper;
    public final String[] extensions;

    Format(FormatMapper mapper, String... extensions) {
        this.mapper = mapper;
        this.extensions = extensions;
    }

}