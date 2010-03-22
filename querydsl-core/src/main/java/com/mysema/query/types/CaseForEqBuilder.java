/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.OString;

/**
 * CaseForEqBuilder enables the construction of typesafe case-when-then-else constructs
 * for equals-operations :
 * e.g.
 * 
 * <pre>
 * QCustomer c = QCustomer.customer;
 * Expr<Integer> cases = c.annualSpending
 *     .when(1000l).then(1)
 *     .when(2000l).then(2)
 *     .when(5000l).then(3)
 *     .otherwise(4);
 * </pre>
 * 
 * @author tiwe
 *
 * @param <D>
 */
public final class CaseForEqBuilder<D> {
    
    private static class CaseElement<D> {
        
        @Nullable
        private final Expr<? extends D> eq;
        
        private final Expr<?> target;
        
        public CaseElement(@Nullable Expr<? extends D> eq, Expr<?> target){
            this.eq = eq;
            this.target = target;
        }

        public Expr<? extends D> getEq() {
            return eq;
        }

        public Expr<?> getTarget() {
            return target;
        }
        
    }
    
    private final Expr<D> base;
    
    private final Expr<? extends D> other;
    
    private final List<CaseElement<D>> caseElements = new ArrayList<CaseElement<D>>();
    
    private Class<?> type;

    public CaseForEqBuilder(Expr<D> base, Expr<? extends D> other) {
        this.base = base;
        this.other = other;
    }
    
    public <T> Cases<T,Expr<T>> then(Expr<T> then){
        type = then.getType();
        return new Cases<T,Expr<T>>(){
            @Override
            protected Expr<T> createResult(Class<T> type, Expr<T> last) {
                return OSimple.create((Class<T>)type, Ops.CASE_EQ, base, last);
            }            
        }.when(other).then(then);
    }
    
    public <T> Cases<T,Expr<T>> then(T then){
        return then(ExprConst.create(then));
    }
    
    public <T extends Number & Comparable<?>> Cases<T,ENumber<T>> then(T then){
        return then(ENumberConst.create(then));
    }
    
    public <T extends Number & Comparable<?>> Cases<T,ENumber<T>> then(ENumber<T> then){
        type = then.getType();
        return new Cases<T,ENumber<T>>(){
            @SuppressWarnings("unchecked")
            @Override
            protected ENumber<T> createResult(Class<T> type, Expr<T> last) {
                return ONumber.create(type, (Operator)Ops.CASE_EQ, base, last);
            }
            
        }.when(other).then(then);
    }
    
    public Cases<String,EString> then(EString then){
        type = then.getType();
        return new Cases<String,EString>(){
            @SuppressWarnings("unchecked")
            @Override
            protected EString createResult(Class<String> type, Expr<String> last) {
                return OString.create((Operator)Ops.CASE_EQ, base, last);
            }
            
        }.when(other).then(then);
    }
    
    public Cases<String,EString> then(String then){
        return then(EStringConst.create(then));
    }

    public abstract class Cases<T, Q extends Expr<T>> {
    
        public CaseWhen<T,Q> when(Expr<? extends D> when){
            return new CaseWhen<T,Q>(this, when);
        }
        
        public CaseWhen<T,Q> when(D when){
            return when(ExprConst.create(when));
        }
        
        @SuppressWarnings("unchecked")
        public Q otherwise(Expr<T> otherwise){
            caseElements.add(0, new CaseElement<D>(null, otherwise));
            Expr<T> last = null;
            for (CaseElement<D> element : caseElements){
                if (last == null){
                    last = OSimple.create((Class<T>)type, Ops.CASE_EQ_ELSE, 
                            element.getTarget());
                }else{
                    last = OSimple.create((Class<T>)type, Ops.CASE_EQ_WHEN, 
                            base,
                            element.getEq(), 
                            element.getTarget(), 
                            last);
                }
            }
            return createResult((Class<T>)type, last);
        }
        
        protected abstract Q createResult(Class<T> type, Expr<T> last);
        
        public Q otherwise(T otherwise){
            return otherwise(ExprConst.create(otherwise));
        }
    }
    
    public class CaseWhen<T, Q extends Expr<T>> {
        
        private final Cases<T, Q> cases;

        private final Expr<? extends D> when;
        
        public CaseWhen(Cases<T, Q> cases, Expr<? extends D> when) {
            this.cases = cases;
            this.when = when;
        }
        
        public Cases<T, Q> then(Expr<T> then){
            caseElements.add(0, new CaseElement<D>(when, then));
            return cases;
        }
        
        public Cases<T, Q> then(T then){
            return then(ExprConst.create(then));
        }
        
    }
    
    
}
