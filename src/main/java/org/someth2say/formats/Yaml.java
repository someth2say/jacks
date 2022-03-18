package org.someth2say.formats;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Yaml extends AbstractFormatMapper<YAMLMapper> implements FormatMapper {

    public Yaml(final YAMLMapper yamlMapper) {
        super(yamlMapper, Format.YAML);
    }

    public Yaml() {
        this(new YAMLMapper());
    }

}