/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mysema.query.grammar.Ops;

/**
 * PathMetadata provides metadata for Path expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class PathMetadata<T> {
    public static final PathType ARRAY_SIZE = new PathType();

    public static final PathType ARRAYVALUE = new PathType();

    public static final PathType ARRAYVALUE_CONSTANT = new PathType();

    public static final PathType LISTVALUE = new PathType();
    
    public static final PathType LISTVALUE_CONSTANT = new PathType();

    public static final PathType MAPVALUE = new PathType();

    public static final PathType MAPVALUE_CONSTANT = new PathType();

    public static final PathType PROPERTY = new PathType();

    public static final PathType SIZE = new PathType();

    public static final PathType VARIABLE = new PathType();
    
    private final Expr<T> expression;

    private final Path<?> parent;

    private final PathType pathType;

    public PathMetadata(Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
    }
    
    public static PathMetadata<Integer> forArrayAccess(Path.PArray<?> parent,
            Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(Path.PArray<?> parent,
            int index) {
        return new PathMetadata<Integer>(parent, Factory.createConstant(index),
                ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(Path.PCollection<?> parent,
            Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, LISTVALUE);
    }    

    public static PathMetadata<Integer> forListAccess(Path.PCollection<?> parent,
            int index) {
        return new PathMetadata<Integer>(parent, Factory.createConstant(index),
                LISTVALUE_CONSTANT);
    }
    
    // bookmark.tags.size
    
    public static <KT> PathMetadata<KT> forMapAccess(Path.PMap<?, ?> parent,
            Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, MAPVALUE);
    }
    public static <KT> PathMetadata<KT> forMapAccess(Path.PMap<?, ?> parent,
            KT key) {
        return new PathMetadata<KT>(parent, Factory.createConstant(key), MAPVALUE_CONSTANT);
    }
    public static PathMetadata<String> forProperty(Path<?> parent,
            String property) {
        return new PathMetadata<String>(parent, Factory.createConstant(property), PROPERTY);
    }
    public static PathMetadata<Integer> forSize(Path.PArray<?> parent) {
        return new PathMetadata<Integer>(parent, null, ARRAY_SIZE);
    } 
    public static PathMetadata<Integer> forSize(Path.PCollection<?> parent) {
        return new PathMetadata<Integer>(parent, null, SIZE);
    }
    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, Factory.createConstant(variable), VARIABLE);
    }
    public Expr<T> getExpression() {
        return expression;
    }
    public Path<?> getParent() {
        return parent;
    }
    public PathType getPathType() {
        return pathType;
    }
    
    public int hashCode(){
        return new HashCodeBuilder()
            .append(expression)
            .append(parent)
            .append(pathType)
            .hashCode();
    }
    
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true; 
        if (obj.getClass() != getClass()) return false;
        PathMetadata<?> p = (PathMetadata<?>)obj;
        return new EqualsBuilder()
            .append(expression, p.expression)
            .append(parent, p.parent)
            .append(pathType, p.pathType)
            .isEquals();
    }
    
    public String toString(){
        if (expression != null){
            return expression.toString();
        }else{
            return super.toString();
        }
    }
    
    /**
     * The Class PathType.
     */
    public static class PathType extends Ops.Op<Path<?>> {}

}
