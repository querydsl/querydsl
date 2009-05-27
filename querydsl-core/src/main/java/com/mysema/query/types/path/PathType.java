package com.mysema.query.types.path;

import com.mysema.query.types.operation.Operator;

/**
 * The Class PathType.
 */
public class PathType extends Operator<Path<?>> {
    private final String symbol;

    public PathType(String symbol) {
        super();
        this.symbol = symbol;
    }

    public String toString() {
        return symbol;
    }
    
    public static final PathType ARRAYVALUE = new PathType("array value");

    public static final PathType ARRAYVALUE_CONSTANT = new PathType("array value constant");

    public static final PathType LISTVALUE = new PathType("list value");

    public static final PathType LISTVALUE_CONSTANT = new PathType("list value constant");

    public static final PathType MAPVALUE = new PathType("map value");

    public static final PathType MAPVALUE_CONSTANT = new PathType("map value constant");

    public static final PathType PROPERTY = new PathType("propery");

    public static final PathType VARIABLE = new PathType("variable");
}