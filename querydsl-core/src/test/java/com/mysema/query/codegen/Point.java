package com.mysema.query.codegen;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.ArrayPath;


public class Point extends ArrayPath<Double>{

    private static final long serialVersionUID = 1776628530121566388L;

    public Point(String variable) {
        super(Double[].class, variable);
    }

    public Point(Path<?> parent, String property) {
        super(Double[].class, parent, property);
    }

    public Point(PathMetadata<?> metadata) {
        super(Double[].class, metadata);
    }

}
