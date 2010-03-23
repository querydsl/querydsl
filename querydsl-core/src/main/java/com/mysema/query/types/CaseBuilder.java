/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.expr.ONumber;
import com.mysema.query.types.expr.OSimple;
import com.mysema.query.types.expr.OString;

/**
 * CaseBuilder enables the construction of typesafe case-when-then-else 
 * constructs :
 * e.g.
 * 
 * <pre>
 * Expr&lt;String&gt; cases = new CaseBuilder()
 *     .when(c.annualSpending.gt(10000)).then("Premier")
 *     .when(c.annualSpending.gt(5000)).then("Gold")
 *     .when(c.annualSpending.gt(2000)).then("Silver")
 *     .otherwise("Bronze");
 * </pre>
 * 
 * @author tiwe
 * 
 */
public final class CaseBuilder {
    
    private static class CaseElement<A> {
        
        @Nullable
        private final EBoolean condition;
        
        private final Expr<A> target;
        
        public CaseElement(@Nullable EBoolean condition, Expr<A> target){
            this.condition = condition;
            this.target = target;
        }

        public EBoolean getCondition() {
            return condition;
        }

        public Expr<A> getTarget() {
            return target;
        }
        
    }
    
    /**
     * Cascading typesafe Case builder
     * 
     * @author tiwe
     *
     * @param <A>
     */
    public abstract static class Cases<A, Q extends Expr<A>> {

        private final List<CaseElement<A>> cases = new ArrayList<CaseElement<A>>();
        
        private final Class<A> type;

        public Cases(Class<A> type) {
            this.type = type;
        }

        Cases<A,Q> addCase(EBoolean condition, Expr<A> expr) {
            cases.add(0, new CaseElement<A>(condition, expr));
            return this;
        }

        protected abstract Q createResult(Class<A> type, Expr<A> last);
        
        public Q otherwise(A constant) {
            return otherwise(ExprConst.create(constant));
        }
        
        public Q otherwise(Expr<A> expr) {
            cases.add(0, new CaseElement<A>(null, expr));
            Expr<A> last = null;
            for (CaseElement<A> element : cases){
                if (last == null){
                    last = OSimple.create(type, Ops.CASE_ELSE, 
                            element.getTarget());
                }else{
                    last = OSimple.create(type, Ops.CASE_WHEN, 
                            element.getCondition(), 
                            element.getTarget(), 
                            last);
                }
            }
            return createResult(type, last);
        }

        public CaseWhen<A,Q> when(EBoolean b) {
            return new CaseWhen<A,Q>(this, b);
        }

    }
    
    /**
     * Intermediate When state
     * 
     * @author tiwe
     *
     * @param <A>
     */
    public static class CaseWhen<A,Q extends Expr<A>> {

        private final EBoolean b;
        
        private final Cases<A,Q> cases;

        public CaseWhen(Cases<A,Q> cases, EBoolean b) {
            this.cases = cases;
            this.b = b;
        }

        public Cases<A,Q> then(A constant) {
            return then(ExprConst.create(constant));
        }

        public Cases<A,Q> then(Expr<A> expr) {
            return cases.addCase(b, expr);
        }
    }

    /**
     * Initial state of Case construction
     * 
     * @author tiwe
     *
     */
    public static class Initial {
        
        private final EBoolean when;

        public Initial(EBoolean b) {
            this.when = b;
        }
        
        public <A> Cases<A,Expr<A>> then(A constant) {
            return then(ExprConst.create(constant));
        }
        
        public Cases<String,EString> then(EString expr){
            return new Cases<String,EString>(String.class){
                @SuppressWarnings("unchecked")
                @Override
                protected EString createResult(Class<String> type, Expr<String> last) {
                    return OString.create((Operator)Ops.CASE, last);
                }
                
            }.addCase(when, expr);
        }

        @SuppressWarnings("unchecked")
        public <A> Cases<A, Expr<A>> then(Expr<A> expr) {
            return new Cases<A,Expr<A>>((Class)expr.getType()){
                @Override
                protected Expr<A> createResult(Class<A> type, Expr<A> last) {
                    return OSimple.create(type, Ops.CASE, last);
                }
                
            }.addCase(when, expr);
        }
        
        @SuppressWarnings("unchecked")
        public <A extends Number & Comparable<?>> Cases<A, ENumber<A>> then(ENumber<A> expr) {
            return new Cases<A, ENumber<A>>((Class)expr.getType()){                
                @Override
                protected ENumber<A> createResult(Class<A> type, Expr<A> last) {
                    return ONumber.create(type, (Operator)Ops.CASE, last);
                }
                
            }.addCase(when, expr);
        }

        public Cases<String, EString> then(String str){
            return then(EStringConst.create(str));
        }
        
        public <A extends Number & Comparable<?>> Cases<A, ENumber<A>> then(A num){
            return then(ENumberConst.create(num));
        }
    }

    public Initial when(EBoolean b) {
        return new Initial(b);
    }

}
