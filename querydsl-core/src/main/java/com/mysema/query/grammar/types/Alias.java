/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

/**
 * Alias provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Alias {
    
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
            
    public static class Simple<D> extends Expr.Simple<D>{     
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
        
//    public interface Simple{ 
//        Expr<?> getFrom();
//        String getTo();
//    }
    
    public interface ToPath{
        Expr<?> getFrom();
        Path<?> getTo();
    }

}
