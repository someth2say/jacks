package org.someth2say.formats;

import org.someth2say.JacksException;

import java.io.InputStream;

public interface FormatMapper {
    InputStream objectToInputStream(final Object obj) throws JacksException;

    Object inputStreamToObject(final InputStream json) throws JacksException;

    <T> T inputStreamToObject(final InputStream json, Class<T> valueType) throws JacksException;

    Object stringToObject(String rawContents) throws JacksException;

    <T> T stringToObject(String rawContents, Class<T> valueType) throws JacksException;

    //TODO: Implement objectToString
}
