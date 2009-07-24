/**
 * 
 */
package com.mysema.query.collections.impl;

import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.types.path.Path;

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