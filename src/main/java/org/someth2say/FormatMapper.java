package org.someth2say;

import java.io.InputStream;

public interface FormatMapper {
    InputStream objectToInputStream(final Object obj) throws yqException;

    Object inputStreamToObject(final InputStream json) throws yqException;

    Format getFormat();

	Object stringToObject(String rawContents) throws yqException;
}
