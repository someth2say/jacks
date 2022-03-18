package org.someth2say.query;

import com.jayway.jsonpath.JsonPathException;
import org.someth2say.JacksException;

public class JsonPath implements QuerySolver {
	public Object query(String query, Object obj) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}

	public <T> T query(String query, Object obj, Class<T> valueType) throws JacksException {
		try {
			return com.jayway.jsonpath.JsonPath.read(obj, query);
		} catch (final JsonPathException e) {
			throw new JacksException("Can not apply JsonPath", e);
		}
	}
}