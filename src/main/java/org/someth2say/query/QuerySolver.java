package org.someth2say.query;

import org.someth2say.JacksException;

public interface QuerySolver {
     Object query(String query, Object obj) throws JacksException;

     <T> T query(String query, Object obj, Class<T> valueType) throws JacksException;
}
