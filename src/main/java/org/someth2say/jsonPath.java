package org.someth2say;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

public class jsonPath {

	public static Object query(String query, Object obj) throws yqException {
		try {
			return JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new yqException("Can not apply JsonPath", e);
		}
	}
    
}