/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;


/**
 * HqlTypes provides general HQL specific types
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlTypes {
    
    private HqlTypes(){}
    
    /**
     * The Class DistinctPath.
     */
    public static class DistinctPath<T> extends Expr<T>{
        private final Path<T> path;
        @SuppressWarnings("unchecked")
        public DistinctPath(Path<T> path) {
            super(((Expr<T>)path).getType());
            this.path = path;
        }
        public Path<T> getPath(){ return path; }
    }
    
}
