package org.someth2say;

import com.jayway.jsonpath.JsonPathException;

public class JsonPath {

	public static Object query(final String query, final Object obj) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}

	public static <T> T query(final String query, final Object obj, final Class<T> valueType) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}
}