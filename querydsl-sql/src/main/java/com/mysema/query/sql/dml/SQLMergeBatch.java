package com.mysema.query.sql.dml;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;

@Immutable
public class SQLMergeBatch {
    
    private final List<Path<?>> keys;
    
    private final List<Path<?>> columns;
    
    private final List<Expression<?>> values;
    
    @Nullable
    private final SubQueryExpression<?> subQuery;
    
    public SQLMergeBatch(List<Path<?>> k, List<Path<?>> c, List<Expression<?>> v, @Nullable SubQueryExpression<?> sq) {
        keys = new ArrayList<Path<?>>(k);
        columns = new ArrayList<Path<?>>(c);
        values = new ArrayList<Expression<?>>(v);
        subQuery = sq;
    }
    
    public List<Path<?>> getKeys(){
        return keys;
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
