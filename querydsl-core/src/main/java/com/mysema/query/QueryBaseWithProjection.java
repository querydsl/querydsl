/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.query.grammar.types.Expr;

/**
 * QueryBaseWithProjection extends the QueryBase interface to provide basic 
 * implementations of the methods of the Projectable itnerface
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class QueryBaseWithProjection<JoinMeta,SubType 
    extends QueryBaseWithProjection<JoinMeta,SubType>> extends QueryBase<JoinMeta,SubType> implements Projectable{

    public QueryBaseWithProjection(){}
    
    public QueryBaseWithProjection(QueryMetadata<JoinMeta> metadata) {
        super(metadata);
    }

    protected <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }
        
    public long countDistinct(){
        getMetadata().setDistinct(true);
        return count();
    }
    
    public final Iterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        getMetadata().setDistinct(true);
        return iterate(first, second, rest);
    }

    public final <RT> Iterator<RT> iterateDistinct(Expr<RT> projection){
        getMetadata().setDistinct(true);
        return iterate(projection);
    }
    
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return IteratorUtils.toList(iterate(first, second, rest));
    }
    
    public <RT> List<RT> list(Expr<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }    
    
    public <RT> SearchResults<RT> listResults(Expr<RT> projection){
    	QueryModifiers modifiers = getMetadata().getModifiers();
    	List<RT> list = list(projection);
    	if (list.isEmpty()){
    		return SearchResults.emptyResults();
    	}else{
    		int start = Math.min(modifiers.getOffset(), list.size());
    		int end = Math.min(modifiers.getOffset() + modifiers.getLimit(), list.size());
    		return new SearchResults<RT>(list.subList(start, end), modifiers, list.size());
    	}    	
    }

    public final List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        getMetadata().setDistinct(true);
        return list(first, second, rest);
    }

    public final <RT> List<RT> listDistinct(Expr<RT> projection) {
        getMetadata().setDistinct(true);
        return list(projection);
    }
    
    public <RT> RT uniqueResult(Expr<RT> expr) {
        Iterator<RT> it = iterate(expr);
        return it.hasNext() ? it.next() : null;
    }
    
    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        Iterator<Object[]> it = iterate(first, second, rest);
        return it.hasNext() ? it.next() : null;
    }
}
