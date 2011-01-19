/**
 * 
 */
package com.mysema.query.sql.dml;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

/**
 * SQLInsertBatch defines the state of an SQL INSERT batch item
 * 
 * @author tiwe
 *
 */
@Immutable
public class SQLInsertBatch {        
    
    private final List<Path<?>> columns;
    
    private final List<Expression<?>> values;
    
    @Nullable
    private final SubQueryExpression<?> subQuery;
    
    public SQLInsertBatch(List<Path<?>> c, List<Expression<?>> v, @Nullable SubQueryExpression<?> sq) {
        columns = new ArrayList<Path<?>>(c);
        values = new ArrayList<Expression<?>>(v);
        subQuery = sq;
    }

    public List<Path<?>> getColumns() {
        return columns;
    }

    public List<Expression<?>> getValues() {
        return values;
    }

    public SubQueryExpression<?> getSubQuery() {
        return subQuery;
    }
    
    
}