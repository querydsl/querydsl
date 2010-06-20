package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PSimple;

/**
 * ForeignKey defines a foreign key on a table to another table
 * 
 * @author tiwe
 *
 * @param <E>
 * @param <P>
 */
public class ForeignKey <E>{
    
    private final PEntity<?> entity;
    
    private final List<? extends Path<?>> localColumns;

    private final List<String> foreignColumns;
    
    public ForeignKey(PEntity<?> entity, Path<?> localColumn, String foreignColumn) {
        this(entity, Arrays.<Path<?>>asList(localColumn), Arrays.asList(foreignColumn));
    }
    
    public ForeignKey(PEntity<?> entity, List<? extends Path<?>> localColumns, List<String> foreignColumns) {
        this.entity = entity;
        this.localColumns = localColumns;
        this.foreignColumns = foreignColumns;
    }
    
    public PEntity<?> getEntity(){
        return entity;
    }

    public List<? extends Path<?>> getLocalColumns() {
        return localColumns;
    }

    public List<String> getForeignColumns() {
        return foreignColumns;
    }
    
    @SuppressWarnings("unchecked")
    public EBoolean on(PEntity<E> entity){
        BooleanBuilder builder = new BooleanBuilder();
        for (int i = 0; i < localColumns.size(); i++){
            Expr local = localColumns.get(i).asExpr();
            Expr foreign = new PSimple(local.getType(), entity, foreignColumns.get(i));
            builder.and(local.eq(foreign));
        }        
        return builder.getValue();
    }
    
}
