/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.JoinExpression;
import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.QueryBaseWithProjection;
import com.mysema.query.types.expr.Expr;

/**
 * 
 * @author tiwe
 *
 * @param <A>
 */
class JDOQLQueryImpl extends QueryBaseWithProjection<Object, JDOQLQueryImpl> implements Projectable, JDOQLQuery {
    
    private static final Logger logger = LoggerFactory.getLogger(JDOQLQueryImpl.class);
   
    private List<Object> constants;
    
    private String countRowsString, queryString;
        
    private final JDOQLOps ops;
    
    private final PersistenceManager pm;
    
    public JDOQLQueryImpl(PersistenceManager pm) {
        this(pm, JDOQLOps.DEFAULT);
    }
        
    public JDOQLQueryImpl(PersistenceManager pm, JDOQLOps ops) {
        this.ops = ops;
        this.pm = pm;
    }

    private String buildQueryString(boolean forCountRow) {
        if (getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No joins given");
        }
        if (getMetadata().getWhere() == null){
        	return null;
        }        
        JDOQLSerializer serializer = new JDOQLSerializer(ops);
        // NOTE : serializes only the constraints
        serializer.handle(getMetadata().getWhere());               
        constants = serializer.getConstants();      
        System.err.println("SERIALIZED : " + serializer.toString());
        return serializer.toString();
    }
    
    @Override
    protected void clear(){
        super.clear();
        queryString = null;
        countRowsString = null;
    }
    
    public long count(){
//        return uniqueResult(HQLGrammar.count());
    	throw new UnsupportedOperationException();
    }

    public long count(Expr<?> expr){
//        return uniqueResult(HQLGrammar.count(expr));
    	throw new UnsupportedOperationException();
    }
    
    private Query createQuery(String queryString, QueryModifiers modifiers) {    	
    	JoinExpression<?> firstJoin = getMetadata().getJoins().get(0);
    	Query query;
    	if (queryString != null){
    		query = pm.newQuery(firstJoin.getTarget().getType(), queryString);
    	}else{
    		query = pm.newQuery(firstJoin.getTarget().getType());	
    	}
    	
    	// variables
    	// TODO
    	
    	// explicit parameters    	
    	if (constants != null){
    		StringBuilder builder = new StringBuilder();
    		for (int i=0; i < constants.size(); i++){
                String key = "a"+(i+1);
                Object val = constants.get(i);       
                if (builder.length() > 0){
                	builder.append(", ");
                }
                builder.append(val.getClass().getName()).append(" ").append(key);            
            }
            query.declareParameters(builder.toString());	
    	}        
        
        // range
        if (modifiers.isRestricting()){
        	long fromIncl = 0;
        	long toExcl = Long.MAX_VALUE;        	
        	if (modifiers.getOffset() != null){
        		fromIncl = modifiers.getOffset().longValue();	
            }
        	if (modifiers.getLimit() != null){
        		toExcl = fromIncl + modifiers.getLimit().longValue();
        	}            
            query.setRange(fromIncl, toExcl);
        }        
        return query;
    }
    
    protected List<Object> getConstants() {
        return constants;
    }
        
    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }
    
    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String queryString = toString();
        return (List<Object[]>) execute(createQuery(queryString, getMetadata().getModifiers()));                
    }
    
	private Object execute(Query query){
    	if (constants != null){
        	return query.executeWithArray(constants.toArray());
        }else{
        	return query.execute();	
        }
    }
    
    @SuppressWarnings("unchecked")
	public <RT> List<RT> list(Expr<RT> expr){
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        return (List<RT>) execute(createQuery(queryString, getMetadata().getModifiers()));        
    }
    
	@SuppressWarnings("unchecked")
	public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(toCountRowsString(),null);
        query.setUnique(true);
        long total = (Long) execute(query);
        if (total > 0) {
        	QueryModifiers modifiers = getMetadata().getModifiers();
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, modifiers);
            List<RT> list = (List<RT>) execute(query);
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }
    
    public String toCountRowsString(){
        if (countRowsString == null){
            countRowsString = buildQueryString(true);    
        }        
        return countRowsString;
    }

    @Override
    public String toString(){
        if (queryString == null){
            queryString = buildQueryString(false);    
        }        
        return queryString;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, QueryModifiers.limit(1));
        query.setUnique(true);
        return (RT)query.execute();
    }

}
