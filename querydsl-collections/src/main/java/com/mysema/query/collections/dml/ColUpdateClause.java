package com.mysema.query.collections.dml;

import com.mysema.query.collections.ColQuery;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.collections.impl.EvaluatorFactory;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class ColUpdateClause<T> implements UpdateClause<ColUpdateClause<T>>{

    private final ColQuery query;
    
    private final Path<T> expr;
    
    public ColUpdateClause(EvaluatorFactory ef, Path<T> expr, Iterable<? extends T> col){
        this.query = new ColQueryImpl(ef).from(expr, col);
        this.expr = expr;
    }
    
    @Override
    public long execute() {
        int rv = 0;
        for (T match : query.list(expr.asExpr())){
            // TODO : update
            rv++;
        }
        return rv;
    }

    @Override
    public <U> ColUpdateClause<T> set(Path<U> path, U value) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ColUpdateClause<T> where(EBoolean... o) {
        query.where(o);
        return this;
    }

}
