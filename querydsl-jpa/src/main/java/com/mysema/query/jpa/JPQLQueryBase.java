/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import java.util.Collection;
import java.util.Map;

import com.mysema.query.QueryMetadata;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.MapPath;

/**
 * HQLQueryBase is a base Query class for HQL
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class JPQLQueryBase<Q extends JPQLQueryBase<Q>> extends ProjectableQuery<Q> {

    private Map<Object,String> constants;

    private final JPQLQueryMixin<Q> queryMixin;

    private final JPQLTemplates templates;

    protected JPQLTemplates getTemplates(){
        return templates;
    }

    protected JPQLQueryMixin<Q> getQueryMixin(){
        return queryMixin;
    }

    @SuppressWarnings("unchecked")
    public JPQLQueryBase(QueryMetadata md, JPQLTemplates templates) {
        super(new JPQLQueryMixin<Q>(md));
        super.queryMixin.setSelf((Q) this);
        this.queryMixin = (JPQLQueryMixin) super.queryMixin;
        this.templates = templates;
    }

    protected String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        JPQLSerializer serializer = new JPQLSerializer(templates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
    }

    public Q fetch(){
        return queryMixin.fetch();
    }

    public Q fetchAll(){
        return queryMixin.fetchAll();
    }

    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    public <P> Q fullJoin(Path<? extends Collection<P>> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q fullJoin(EntityPath<P> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q fullJoin(MapPath<?,P,?> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    protected Map<Object,String> getConstants() {
        return constants;
    }

    public <P> Q innerJoin(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(MapPath<?,P,?> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q join(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q join(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(MapPath<?,P,?> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q leftJoin(Path<? extends Collection<P>> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(EntityPath<P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(MapPath<?,P,?> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public Q with(BooleanExpression... conditions){
        return queryMixin.with(conditions);
    }

    protected void setConstants(Map<Object, String> constants) {
        this.constants = constants;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString(){
        return buildQueryString(false);
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

}
