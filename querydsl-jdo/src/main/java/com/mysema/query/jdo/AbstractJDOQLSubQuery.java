package com.mysema.query.jdo;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q>
 */
public class AbstractJDOQLSubQuery<Q extends AbstractJDOQLSubQuery<Q>> extends DetachableQuery<Q>{

    public AbstractJDOQLSubQuery() {
        this(new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractJDOQLSubQuery(QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q)this);
    }

    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    @SuppressWarnings("unchecked")
    public <P> Q from(Path<? extends Collection<P>> target, EntityPath<P> alias){
        queryMixin.getMetadata().addJoin(JoinType.DEFAULT, SimpleOperation.create(alias.getType(), Ops.ALIAS, target, alias));
        return (Q)this;
    }

    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expression<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(JDOQLTemplates.DEFAULT, source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

}
