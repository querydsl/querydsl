/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Types.*;

/**
 * VisitorAdapter provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorAdapter<V extends VisitorAdapter<V>> extends Visitor<V>{
    
    @Override
    protected void visit(AliasForAnything<?> expr) {
        visit((Alias<?>)expr);        
    }
    
    @Override
    protected void visit(AliasForCollection<?> expr){
        visit((Alias<?>)expr);
    }
    
    @Override
    protected void visit(AliasForEntity<?> expr) {
        visit((Alias<?>)expr);        
    }
    
    @Override
    protected void visit(BinaryBooleanOperation<?,?> expr) {
        visit((BinaryOperation<?,?,?,?>)expr);
    }
    
    @Override
    protected void visit(RefBoolean expr) {
        visit((Reference<?>)expr);     
    }
    
    @Override
    protected void visit(RefCollection<?> expr){
        visit((Reference<?>)expr);
    }
    
    @Override
    protected void visit(RefDomainType<?> expr) {
        visit((Reference<?>)expr);        
    }
    
    @Override
    protected void visit(TertiaryBooleanOperation<?,?,?> expr) {
        visit((TertiaryOperation<?,?,?,?,?>)expr);
    }
    
    @Override
    protected void visit(UnaryBooleanOperation<?> expr) {
        visit((UnaryOperation<?,?,?>)expr);        
    }
    
}
