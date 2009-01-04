/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Expr.EEntity;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.Path.PEntity;
import com.mysema.query.grammar.types.Path.PEntityCollection;

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
    public static class AEntity<D> extends EEntity<D> implements AToPath{
        private final Expr<?> from;
        private final Path<?> to;
        public AEntity(PEntity<D> from, PEntity<D> to) {
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
    public static class AEntityCollection<D> extends EEntity<D> implements AToPath{
        private final Expr<?> from;
        private final Path<?> to;
        public AEntityCollection(PEntityCollection<D> from, Path<D> to) {
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
    public static class ASimple<D> extends ESimple<D> implements Alias{     
        private final Expr<?> from;
        private final String to;
        public ASimple(Expr<D> from, String to) {
            super(from.getType());
            this.from = from;
            this.to = to;
        }
        public Expr<D> as(String to) {
            return IntGrammar.as(this, to);
        }   
        public Expr<?> getFrom() {return from;}
        public String getTo() {return to;}  
    }
    
    /**
     * The Interface To
     */
    public interface AToPath extends Alias{        
        Path<?> getTo();
    }

}
