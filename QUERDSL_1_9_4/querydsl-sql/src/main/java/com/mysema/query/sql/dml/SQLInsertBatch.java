/**
 * 
 */
package com.mysema.query.sql.dml;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

/**
 * @author tiwe
 *
 */
public class SQLInsertBatch {        
    
    private final List<Path<?>> columns = new ArrayList<Path<?>>();
    
    private final List<Expr<?>> values = new ArrayList<Expr<?>>();
    
    @Nullable
    private final SubQueryExpression<?> subQuery;
    
    public SQLInsertBatch(List<Path<?>> c, List<Expr<?>> v, @Nullable SubQueryExpression<?> sq) {
        columns.addAll(c);
        values.addAll(v);
        subQuery = sq;
    }

    public List<Path<?>> getColumns() {
        return columns;
    }

    public List<Expr<?>> getValues() {
        return values;
    }

    public SubQueryExpression<?> getSubQuery() {
        return subQuery;
    }
    
    
}