/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.PathMetadata;
import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.PathEntity;
import com.mysema.query.grammar.Types.PathNoEntitySimple;

/**
 * HqlQuery provides a fluent statically typed interface for creating HQL queries
 *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class HqlQuery extends HqlQueryBase<HqlQuery>{

    private static final Logger logger = LoggerFactory.getLogger(HqlQuery.class);

    private Integer limit, offset;
    
    private final Session session;

    public HqlQuery(Session session) {
        this.session = session;
    }

    private Query createQuery(String queryString, Integer limit, Integer offset) {
        Query query = session.createQuery(queryString);
        List<?> constants = getConstants();
        for (int i=0; i < constants.size(); i++){
            String key = "a"+(i+1);
            Object val = constants.get(i);            
            if (val instanceof Collection<?>){
                query.setParameterList(key,(Collection<?>)val);
            }else if (val.getClass().isArray()){
                query.setParameterList(key,(Object[])val);
            }else{
                query.setParameter(key,val);    
            }
        }
        if (limit != null) query.setMaxResults(limit);
        if (offset != null) query.setFirstResult(offset);
        return query;
    }
    
    public HqlQuery forExample(PathEntity<?> entity, Map<String, Object> map) {
        select(entity).from(entity);
        try {            
            List<ExprBoolean> conds = new ArrayList<ExprBoolean>(map.size());  
            for (Map.Entry<String, Object> entry : map.entrySet()){                
                if (!entry.getKey().equals("id") 
                        && !entry.getKey().equals("class")
                        && !entry.getKey().equals("created")
                        && !entry.getKey().equals("modified")){
                    PathMetadata md = PathMetadata.forProperty(entity, entry.getKey());
                    PathNoEntitySimple path = new PathNoEntitySimple(Object.class, md);
                    if (entry.getValue() != null){
                        conds.add(path.eq(entry.getValue()));
                    }else{
                        conds.add(path.isnull());                        
                    }                    
                }
            }            
            where(conds.toArray(new ExprBoolean[conds.size()]));
            return this;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }

    public HqlQuery limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public List list() {
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.list();
    }    


    public SearchResults<?> listResults() {
        Query query = createQuery(toCountRowsString(), null, null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, limit, offset);
            List list = query.list();
            return new SearchResults(list,
                    limit == null ? Long.MAX_VALUE : limit.longValue(),
                    offset == null ? 0l : offset.longValue(), total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    public HqlQuery offset(int offset) {
        this.offset = offset;
        return this;
    }

    public HqlQuery restrict(QueryModifiers mod) {
        this.limit = mod.getLimit();
        this.offset = mod.getOffset();
        return this;
    }

    public Object uniqueResult() {
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, 1, null);
        return query.uniqueResult();
    }

}
