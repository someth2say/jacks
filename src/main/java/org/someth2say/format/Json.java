package org.someth2say.format;

import com.fasterxml.jackson.databind.json.JsonMapper;

public class Json extends AbstractFormatMapper<JsonMapper> implements FormatMapper {

    public Json(final JsonMapper jsonMapper) {
        super(jsonMapper, Format.JSON);
    }

    public Json() {
        this(new JsonMapper());
    }

}