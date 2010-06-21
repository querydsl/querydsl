package com.mysema.query.jdoql;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.OSimple;
import com.mysema.query.types.path.PEntity;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q>
 */
public class AbstractJDOQLSubQuery<Q extends AbstractJDOQLSubQuery<Q>> extends DetachableQuery<Q>{

    /**
     *
     */
    public AbstractJDOQLSubQuery() {
        this(new DefaultQueryMetadata());
    }

    /**
     * @param metadata
     */
    @SuppressWarnings("unchecked")
    public AbstractJDOQLSubQuery(QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q)this);
    }

    public Q from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    @SuppressWarnings("unchecked")
    public <P> Q from(Path<? extends Collection<P>> target, PEntity<P> alias){
        queryMixin.getMetadata().addJoin(JoinType.DEFAULT, OSimple.create(alias.getType(), Ops.ALIAS, target.asExpr(), alias));
        return (Q)this;
    }

    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(JDOQLTemplates.DEFAULT, source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

}
