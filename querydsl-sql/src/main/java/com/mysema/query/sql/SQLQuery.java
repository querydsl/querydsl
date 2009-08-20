package com.mysema.query.sql;

import com.mysema.query.Detachable;
import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 */
public interface SQLQuery extends Query<SQLQuery>, Projectable, Detachable {
    
    SQLQuery from(PEntity<?>... o);

    SQLQuery fullJoin(Expr<?> o);

    SQLQuery innerJoin(Expr<?> o);

    SQLQuery join(Expr<?> o);

    SQLQuery leftJoin(Expr<?> o);
    
    SQLQuery on(EBoolean condition);
    
    <RT> Union<RT> union(ObjectSubQuery<RT>... sq);
    
    <RT> Union<RT> union(ListSubQuery<RT>... sq);

}
