/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.expr.EBoolean;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 */
public class AbstractSQLSubQuery<Q extends AbstractSQLSubQuery<Q>> extends DetachableQuery<Q> {

    public AbstractSQLSubQuery() {
        this(new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLSubQuery(QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q)this);
    }

    protected Q addFlag(Position position, String prefix, Expr<?> expr){
        Expr<?> flag = CSimple.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    protected Q addFlag(Position position, String flag){
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    protected Q addFlag(Position position, Expr<?> flag){
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    public Q from(Expr<?>... args){
        return queryMixin.from(args);
    }

    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    public Q fullJoin(RelationalPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    public Q fullJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public Q innerJoin(RelationalPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    public Q innerJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    public Q join(RelationalPath<?> target) {
        return queryMixin.join(target);
    }

    public Q join(SubQuery<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    public Q leftJoin(RelationalPath<?> target) {
        return queryMixin.leftJoin(target);
    }   

    public Q leftJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public Q on(EBoolean... conditions){
        return queryMixin.on(conditions);
    }

    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    public Q rightJoin(RelationalPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    public Q rightJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            SQLSerializer serializer = new SQLSerializer(SQLTemplates.DEFAULT);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

}
