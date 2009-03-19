package com.mysema.query.collections.support;

import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.grammar.types.Path;

/**
 * Mapping from root path to indexed path and evaluator for the index key
 * 
 */
public class IndexedPath {
    private final Path<?> path;

    private final Evaluator ev;

    IndexedPath(Path<?> path, Evaluator ev) {
        this.path = path;
        this.ev = ev;
    }

    public Path<?> getIndexedPath() {
        return path;
    }

    public Evaluator getEvaluator() {
        return ev;
    }
}