/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;



/**
 * VisitorAdapter provides a base implementation where invocations are
 * dispatched to supertypes when available and visible
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorAdapter<V extends VisitorAdapter<V>> extends Visitor<V> {

    @Override
    protected void visit(Alias.Entity<?> expr) {
        visit((Alias.ToPath) expr);
    }
    @Override
    protected void visit(Alias.EntityCollection<?> expr) {
        visit((Alias.ToPath) expr);
    }
//    @Override
//    protected void visit(Alias.Literal<?> expr) {
//        visit((Alias.Simple) expr);
//    }
    @Override
    protected void visit(Operation.Boolean expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.Comparable<?,?> expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.Number<?,?> expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Operation.String expr) {
        visit((Operation<?, ?>) expr);
    }
    @Override
    protected void visit(Path.Boolean expr) {
        visit((Path<?>) expr);
    }
    protected void visit(Path.Collection<?> expr){
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.Comparable<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.ComponentCollection<?> expr) {
        visit((Path.Collection<?>) expr);
    }    
    @Override
    protected void visit(Path.ComponentMap<?,?> expr) {
        visit((Path.Map<?,?>) expr);
    }    
    @Override
    protected void visit(Path.Entity<?> expr) {
        visit((Path<?>) expr);
    }    
    @Override
    protected void visit(Path.EntityCollection<?> expr) {
        visit((Path.Collection<?>) expr);
    }
    @Override
    protected void visit(Path.EntityMap<?,?> expr) {
        visit((Path.Map<?,?>) expr);
    }
    @Override
    protected void visit(Path.RenamableEntity<?> expr) {
        visit((Path.Entity<?>) expr);
    }
    protected void visit(Path.Map<?,?> expr){
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.Literal<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.SimpleLiteral<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(Path.String expr) {
        visit((Path<?>) expr);
    }
}
