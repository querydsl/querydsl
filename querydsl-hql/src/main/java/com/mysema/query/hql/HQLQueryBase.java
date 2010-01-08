/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.Path;

/**
 * HQLQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HQLQueryBase<SubType extends HQLQueryBase<SubType>> extends ProjectableQuery<SubType> {

    private Map<Object,String> constants;

    @Nullable
    private String countRowsString, queryString;

    protected final HQLQueryMixin<SubType> queryMixin;
    
    private final HQLTemplates templates;
    
    @SuppressWarnings("unchecked")
    public HQLQueryBase(QueryMetadata md, HQLTemplates templates) {
        super(new HQLQueryMixin<SubType>(md));
        super.queryMixin.setSelf((SubType) this);
        this.queryMixin = (HQLQueryMixin) super.queryMixin;
        this.templates = templates;
    }

    private String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }
    
    public SubType fetch(){
        return queryMixin.fetch();
    }

    public SubType fetchAll(){
        return queryMixin.fetchAll();
    }

    public SubType from(PEntity<?>... args) {        
        return queryMixin.from(args);
    }

    public <P> SubType fullJoin(Path<? extends Collection<P>> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> SubType fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> SubType fullJoin(PEntity<P> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> SubType fullJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> SubType fullJoin(PMap<?,P,?> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> SubType fullJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    protected Map<Object,String> getConstants() {
        return constants;
    }
    
    public <P> SubType innerJoin(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> SubType innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> SubType innerJoin(PEntity<P> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> SubType innerJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> SubType innerJoin(PMap<?,P,?> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> SubType innerJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> SubType join(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> SubType join(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> SubType join(PEntity<P> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> SubType join(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> SubType join(PMap<?,P,?> target) {
        return queryMixin.join(target);
    }
    
    public <P> SubType join(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> SubType leftJoin(Path<? extends Collection<P>> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> SubType leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> SubType leftJoin(PEntity<P> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> SubType leftJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> SubType leftJoin(PMap<?,P,?> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> SubType leftJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public SubType with(EBoolean... conditions){
        return queryMixin.with(conditions);
    }
    
    protected String toCountRowsString() {
        if (countRowsString == null) {
            countRowsString = buildQueryString(true);
        }
        return countRowsString;
    }

    protected String toQueryString(){
      if (queryString == null) {
          queryString = buildQueryString(false);
      }
      return queryString;
    }
    
    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }


}
