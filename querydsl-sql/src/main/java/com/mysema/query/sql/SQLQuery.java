package com.mysema.query.sql;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * Query interface for SQL queries
 * 
 * @author tiwe
 *
 */
public interface SQLQuery extends Query<SQLQuery>, Projectable {
    
    /**
     * @param o
     * @return
     */
    SQLQuery from(PEntity<?>... o);

    /**
     * @param o
     * @return
     */
    SQLQuery fullJoin(Expr<?> o);

    /**
     * @param o
     * @return
     */
    SQLQuery innerJoin(Expr<?> o);

    /**
     * @param o
     * @return
     */
    SQLQuery join(Expr<?> o);

    /**
     * @param o
     * @return
     */
    SQLQuery leftJoin(Expr<?> o);
    
    /**
     * @param conditions
     * @return
     */
    SQLQuery on(EBoolean... conditions);
    
    /**
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ObjectSubQuery<RT>... sq);
    
    /**
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ListSubQuery<RT>... sq);

}
