package com.mysema.query.hql.jpa;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * 
 * 
 * @author tiwe
 *
 */
public interface JPAQLQuery extends Projectable{
    
    JPAQLQuery from(PEntity<?>... o);

    JPAQLQuery innerJoin(EEntity<?> target, PEntity<?> alias);
    
    JPAQLQuery join(EEntity<?> target, PEntity<?> alias);
    
    JPAQLQuery fullJoin(EEntity<?> target, PEntity<?> alias);
    
    JPAQLQuery leftJoin(EEntity<?> target, PEntity<?> alias);
    
    JPAQLQuery on(EBoolean cond);
    
    JPAQLQuery groupBy(Expr<?>... e);
    
    JPAQLQuery having(EBoolean... cond);
    
    JPAQLQuery where(EBoolean... o);
    
    JPAQLQuery orderBy(OrderSpecifier<?>... o);
        
    JPAQLQuery limit(long limit);

    JPAQLQuery offset(long offset);

    JPAQLQuery restrict(QueryModifiers mod);

}
