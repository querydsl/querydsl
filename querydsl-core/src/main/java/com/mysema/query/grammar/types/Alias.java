/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

/**
 * Alias represents alias expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Alias {
    
    Expr<?> getFrom();
    
    /**
     * The Class Entity.
     */
    public static class Entity<D> extends Expr.Entity<D> implements ToPath{
        private final Expr<?> from;
        private final Path<?> to;
        public Entity(Path.Entity<D> from, Path.Entity<D> to) {
            super(from.getType());
            this.from = from;
            this.to = to;
        }
        public Expr<?> getFrom() {return from;}
        public  Path<?> getTo() {return to;}  
    }
    
    /**
     * The Class EntityCollection.
     */
    public static class EntityCollection<D> extends Expr.Entity<D> implements ToPath{
        private final Expr<?> from;
        private final Path<?> to;
        public EntityCollection(Path.EntityCollection<D> from, Path<D> to) {
            super(null);
            this.from = from;
            this.to = to;
        }
        public Expr<?> getFrom() {return from;}
        public  Path<?> getTo() {return to;}        
    }
            
    /**
     * The Class Simple.
     */
    public static class Simple<D> extends Expr.Simple<D> implements Alias{     
        private final Expr<?> from;
        private final java.lang.String to;
        public Simple(Expr<D> from, java.lang.String to) {
            super(from.getType());
            this.from = from;
            this.to = to;
        }
        public Expr<D> as(java.lang.String to) {
            return IntGrammar.as(this, to);
        }   
        public Expr<?> getFrom() {return from;}
        public java.lang.String getTo() {return to;}  
    }
    
    /**
     * The Interface ToPath.
     */
    public interface ToPath extends Alias{        
        Path<?> getTo();
    }

}
