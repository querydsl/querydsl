/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.lang.reflect.Method;

import com.mysema.core.collection.FactoryMap;
import com.mysema.query.grammar.Types.*;

/**
 * Visitor provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class Visitor<T extends Visitor<T>> {
    
    private final FactoryMap<Class<?>,Method> methodMap = new FactoryMap<Class<?>,Method>(){

        @Override
        protected Method create(Class<?> cl) {
            while (!Types.class.equals(cl.getEnclosingClass())){
                cl = cl.getSuperclass();
            }
            try {
                Method method = null;
                Class<?> sigClass = Visitor.this.getClass();
                while (method == null && !sigClass.equals(Visitor.class)){
                    try{
                        method = sigClass.getDeclaredMethod("visit", cl);    
                    }catch(NoSuchMethodException nsme){
                        sigClass = sigClass.getSuperclass();
                    }                       
                }
                if (method != null){
                    method.setAccessible(true);    
                }                
                return method;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
    };
    
    @SuppressWarnings("unchecked")
    public final T handle(Expr<?> expr){
        try {
            methodMap.get(expr.getClass()).invoke(this, expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) this;
    }
    
    protected abstract void visit(Alias<?> expr);
    
    protected abstract void visit(BinaryBooleanOperation<?,?> expr);
    
    protected abstract void visit(BinaryOperation<?,?,?,?> expr);
    
    protected abstract void visit(BooleanProperty expr);
    
    protected abstract void visit(CollectionAlias<?> expr);
    
    protected abstract void visit(CollectionReference<?> expr);
    
    protected abstract void visit(ConstantExpr<?> expr);
    
    protected abstract void visit(DomainType<?> expr);
    
    protected abstract void visit(Reference<?> expr);
    
    protected abstract void visit(TertiaryBooleanOperation<?,?,?> expr);
    
    protected abstract void visit(TertiaryOperation<?,?,?,?,?> expr);
    
    protected abstract void visit(UnaryBooleanOperation<?> expr);
    
    protected abstract void visit(UnaryOperation<?,?,?> expr);
}
