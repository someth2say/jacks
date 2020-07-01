package org.someth2say.formats;

import java.io.InputStream;

import org.someth2say.yqException;

public interface FormatMapper {
    InputStream objectToInputStream(final Object obj) throws yqException;

    Object inputStreamToObject(final InputStream json) throws yqException;

    <T> T inputStreamToObject(final InputStream json, Class<T> valueType) throws yqException;

    Object stringToObject(String rawContents) throws yqException;
    
    <T> T stringToObject(String rawContents, Class<T> valueType) throws yqException;
    
}
