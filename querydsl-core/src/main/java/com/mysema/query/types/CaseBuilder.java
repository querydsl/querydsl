package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;

/**
 * @author tiwe
 * 
 */
public class CaseBuilder {
    
    private class CaseElement<A> {
        
        final EBoolean condition;
        
        final Expr<A> target;
        
        public CaseElement(EBoolean condition, Expr<A> target){
            this.condition = condition;
            this.target = target;
        }
    }
    
    public class Cases<A> {

        private final List<CaseElement<A>> cases = new ArrayList<CaseElement<A>>();
        
        private final Class<A> type;

        public Cases(Class<A> type) {
            this.type = type;
        }

        Cases<A> addCase(EBoolean condition, Expr<A> expr) {
            cases.add(0, new CaseElement<A>(condition, expr));
            return this;
        }

        public Expr<A> otherwise(A constant) {
            return otherwise(Expr.create(constant));
        }
        
        public Expr<A> otherwise(Expr<A> expr) {
            cases.add(0, new CaseElement<A>(null, expr));
            Expr<A> last = null;
            for (CaseElement<A> element : cases){
                if (last == null){
                    last = OSimple.create(type, Ops.CASE_ELSE, 
                            element.target);
                }else{
                    last = OSimple.create(type, Ops.CASE_WHEN, 
                            element.condition, 
                            element.target, 
                            last);
                }
            }
            return OSimple.create(type, Ops.CASE, last);
        }

        public CaseWhen<A> when(EBoolean b) {
            return new CaseWhen<A>(this, b);
        }

    }
    
    public class CaseWhen<A> {

        private final EBoolean b;
        
        private final Cases<A> cases;

        public CaseWhen(Cases<A> cases, EBoolean b) {
            this.cases = cases;
            this.b = b;
        }

        public Cases<A> then(A constant) {
            return then(Expr.create(constant));
        }

        public Cases<A> then(Expr<A> expr) {
            return cases.addCase(b, expr);
        }
    }

    public class Initial {
        
        private final EBoolean when;

        public Initial(EBoolean b) {
            this.when = b;
        }

        public <A> Cases<A> then(A constant) {
            return then(Expr.create(constant));
        }

        @SuppressWarnings("unchecked")
        public <A> Cases<A> then(Expr<A> expr) {
            return new Cases<A>((Class)expr.getType()).addCase(when, expr);
        }
    }

    public Initial when(EBoolean b) {
        return new Initial(b);
    }

}
