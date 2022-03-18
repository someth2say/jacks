package org.someth2say.query;

import java.util.function.Supplier;

public enum QueryLang {
    JSONPATH(JsonPath::new),
    XPATH(XPath::new);

    private QuerySolver solver;
    final Supplier<QuerySolver> solverSupplier;

    QueryLang(final Supplier<QuerySolver> solverSupplier) {
        this.solverSupplier = solverSupplier;
    }

    public QuerySolver getSolver() {
        return solver != null ? solver : (solver = solverSupplier.get());
    }
}
