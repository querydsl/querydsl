package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;

/**
 * @author tiwe
 * 
 */
public class CaseBuilder {
    
    public class Cases<A> {

        private final List<Expr<?>> exprs = new ArrayList<Expr<?>>();
        
        private final Class<A> type;

        public Cases(Class<A> type) {
            this.type = type;
        }

        Cases<A> addCase(EBoolean condition, Expr<A> expr) {
            exprs.add(OSimple.create(type, Ops.CASE_WHEN, condition, expr));
            return this;
        }

        private Expr<A> createChain(List<Expr<?>> exprs) {
            StringBuilder builder = new StringBuilder(exprs.size() * 4);
            for (int i = 0; i < exprs.size(); i++){
                if (i > 0) builder.append(" ");
                builder.append("{").append(i).append("}");
            }
            return new CSimple<A>(type, exprs, templateFactory.create(builder.toString()));
        }

        public Expr<A> otherwise(A constant) {
            return otherwise(Expr.create(constant));
        }
        
        public Expr<A> otherwise(Expr<A> expr) {
            exprs.add(OSimple.create(type, Ops.CASE_ELSE, expr));
            return OSimple.create(type, Ops.CASE, createChain(exprs));
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
            return cases.addCase(b, Expr.create(constant));
        }

        public Cases<A> then(Expr<A> expr) {
            return cases.addCase(b, expr);
        }
    }

    public class Initial {
        
        private final EBoolean b;

        public Initial(EBoolean b) {
            this.b = b;
        }

        @SuppressWarnings("unchecked")
        public <A> Cases<A> then(A constant) {
            return new Cases<A>((Class) constant.getClass()).addCase(b, Expr.create(constant));
        }

        @SuppressWarnings("unchecked")
        public <A> Cases<A> then(Expr<A> expr) {
            return new Cases<A>((Class)expr.getType()).addCase(b, expr);
        }
    }

    private static final TemplateFactory templateFactory = new TemplateFactory();
    

    public Initial when(EBoolean b) {
        return new Initial(b);
    }

}
