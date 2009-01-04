/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;



/**
 * VisitorAdapter provides a base implementation where invocations are
 * dispatched to supertypes when available and visible.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorAdapter<V extends VisitorAdapter<V>> extends Visitor<V> {

    @Override
    protected void visit(Alias.AEntity<?> expr) {
        visit((Alias.AToPath) expr);
    }
    @Override
    protected void visit(Alias.AEntityCollection<?> expr) {
        visit((Alias.AToPath) expr);
    }
    @Override
    protected void visit(Operation.OBoolean expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.OComparable<?,?> expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.ONumber<?,?> expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.OString expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.OStringArray expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Path.PArray<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PBoolean expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PBooleanArray expr) {
        visit((Path.PArray<?>) expr);
    }
    protected void visit(Path.PCollection<?> expr){
        visit((Path<?>) expr);
    }
    protected void visit(Path.PList<?> expr){
        visit((Path.PCollection<?>) expr);
    }
    @Override
    protected void visit(Path.PComparable<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PComparableArray<?> expr) {
        visit((Path.PArray<?>) expr);
    }
    @Override
    protected void visit(Path.PComponentCollection<?> expr) {
        visit((Path.PCollection<?>) expr);
    }
    @Override
    protected void visit(Path.PComponentList<?> expr) {
        visit((Path.PList<?>) expr);
    }    
    @Override
    protected void visit(Path.PComponentMap<?,?> expr) {
        visit((Path.PMap<?,?>) expr);
    }    
    @Override
    protected void visit(Path.PEntity<?> expr) {
        visit((Path<?>) expr);
    }    
    @Override
    protected void visit(Path.PEntityCollection<?> expr) {
        visit((Path.PCollection<?>) expr);
    }
    @Override
    protected void visit(Path.PEntityList<?> expr) {
        visit((Path.PList<?>) expr);
    }
    @Override
    protected void visit(Path.PEntityMap<?,?> expr) {
        visit((Path.PMap<?,?>) expr);
    }
    @Override
    protected void visit(Path.PMap<?,?> expr){
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PSimple<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PString expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.PStringArray expr) {
        visit((Path.PArray<?>) expr);
    }
}
