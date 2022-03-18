package org.someth2say.format;

import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class Yaml extends AbstractFormatMapper<YAMLMapper> implements FormatMapper {

    public Yaml(final YAMLMapper yamlMapper) {
        super(yamlMapper, Format.YAML);
    }

    public Yaml() {
        this(YAMLMapper.builder().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER).build());
    }

}