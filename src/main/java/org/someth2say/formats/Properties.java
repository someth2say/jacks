package org.someth2say.formats;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

public class Properties extends AbstractFormatMapper<JavaPropsMapper> implements FormatMapper {

    public Properties(final JavaPropsMapper propsMapper) {
        super(propsMapper, Format.PROPS);
    }

    public Properties() {
        this(new JavaPropsMapper());
    }

}