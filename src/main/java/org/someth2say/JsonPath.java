package org.someth2say;

import com.jayway.jsonpath.JsonPathException;

public class JsonPath {

	public static Object query(String query, Object obj) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}

	public static <T> T query(String query, Object obj, Class<T> valueType) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.<T>read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}
}