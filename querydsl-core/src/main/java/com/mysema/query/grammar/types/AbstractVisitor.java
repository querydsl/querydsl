/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.types.Alias.AEntity;
import com.mysema.query.grammar.types.Alias.AEntityCollection;
import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.Path.*;



/**
 * VisitorAdapter provides a base implementation where invocations are
 * dispatched to supertypes when available and visible.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractVisitor<SubType extends AbstractVisitor<SubType>> extends Visitor<SubType> {

    @Override
    protected void visit(AEntity<?> expr) {
        visit((AToPath) expr);
    }
    @Override
    protected void visit(AEntityCollection<?> expr) {
        visit((AToPath) expr);
    }
    @Override
    protected void visit(Custom.CBoolean expr){
        visit((Custom<?>)expr);
    }
    @Override
    protected void visit(Custom.CComparable<?> expr){
        visit((Custom<?>)expr);
    }
    @Override
    protected void visit(Custom.CSimple<?> expr){
        visit((Custom<?>)expr);
    }
    @Override
    protected void visit(Custom.CString expr){
        visit((Custom<?>)expr);
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
    protected void visit(PArray<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PBoolean expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PBooleanArray expr) {
        visit((PArray<?>) expr);
    }
    protected void visit(PCollection<?> expr){
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PComparable<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PComparableArray<?> expr) {
        visit((PArray<?>) expr);
    }
    @Override
    protected void visit(PComponentCollection<?> expr) {
        visit((PCollection<?>) expr);
    }    
    @Override
    protected void visit(PComponentList<?> expr) {
        visit((PList<?>) expr);
    }
    @Override
    protected void visit(PComponentMap<?,?> expr) {
        visit((PMap<?,?>) expr);
    }    
    @Override
    protected void visit(PEntity<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PEntityCollection<?> expr) {
        visit((PCollection<?>) expr);
    }
    @Override
    protected void visit(PEntityList<?> expr) {
        visit((PList<?>) expr);
    }
    @Override
    protected void visit(PEntityMap<?,?> expr) {
        visit((PMap<?,?>) expr);
    }
    protected void visit(PList<?> expr){
        visit((PCollection<?>) expr);
    }
    @Override
    protected void visit(PMap<?,?> expr){
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PNumber<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PSimple<?> expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PString expr) {
        visit((Path<?>) expr);
    }
    @Override
    protected void visit(PStringArray expr) {
        visit((PArray<?>) expr);
    }
    
}
