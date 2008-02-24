/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Types.*;

/**
 * VisitorAdapter provides a base implementation where invocations are dispatched
 * to supertypes when available and visible
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class VisitorAdapter<V extends VisitorAdapter<V>> extends Visitor<V>{
    
    @Override
    protected void visit(AliasForNoEntity<?> expr) {
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
    protected void visit(OperationBinaryBoolean<?,?> expr) {
        visit((OperationBinary<?,?,?,?>)expr);
    }
    
    @Override
    protected void visit(PathForBoolean expr) {
        visit((Path<?>)expr);     
    }
    
    @Override
    protected void visit(PathForEntityCollection<?> expr){
        visit((Path<?>)expr);
    }
    
    @Override
    protected void visit(PathForEntity<?> expr) {
        visit((Path<?>)expr);        
    }
    
    @Override
    protected void visit(PathForNoEntity<?> expr) {
        visit((Path<?>)expr);        
    }
    
    @Override
    protected void visit(OperationTertiaryBoolean<?,?,?> expr) {
        visit((OperationTertiary<?,?,?,?,?>)expr);
    }
    
    @Override
    protected void visit(OperationUnaryBoolean<?> expr) {
        visit((OperationUnary<?,?,?>)expr);        
    }
    
}
