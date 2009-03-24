/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.collections.utils.QueryIteratorUtils.multiArgFilter;
import static com.mysema.query.collections.utils.QueryIteratorUtils.toArrayIterator;
import static com.mysema.query.collections.utils.QueryIteratorUtils.transform;

import java.io.IOException;
import java.util.*;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.iterators.IteratorChain;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.support.DefaultIndexSupport;
import com.mysema.query.collections.support.DefaultSourceSortingSupport;
import com.mysema.query.collections.support.MultiComparator;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.util.CloseableIterator;

/**
 * AbstractColQuery provides a base class for Collection query implementations.
 * Extend it like this
 * 
 * <pre>
 * public class MyType extends AbstractColQuery<MyType>{
 *   ...
 * }
 * </pre>
 * 
 * @see ColQuery
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractColQuery<SubType extends AbstractColQuery<SubType>> {
        
    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType)this;
    
    private final Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();
    
    private QueryIndexSupport indexSupport;
    
    private final JavaOps ops;

    private final InnerQuery query = new InnerQuery();
    
    private boolean sortSources = true, wrapIterators = true, sequentialUnion = false;
    
    private SourceSortingSupport sourceSortingSupport;

    public AbstractColQuery() {
        this(JavaOps.DEFAULT);
    }
    
    public AbstractColQuery(JavaOps ops) {
        this.ops = ops;
        this.sourceSortingSupport = new DefaultSourceSortingSupport();
    }
    
    protected QueryIndexSupport createIndexSupport(Map<Expr<?>, Iterable<?>> exprToIt, JavaOps ops, List<Expr<?>> sources){
        return new DefaultIndexSupport(new SimpleIteratorSource(exprToIt), ops, sources);
    }
    
    public void close(){
        // overwrite
    }
    
    protected <A> SubType alias(Expr<A> path, Iterable<A> col) {
        exprToIt.put(path, col);
        return _this;
    }

    private <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }

    public <A> SubType from(Expr<A> entity, A first, A... rest) {
        List<A> list = new ArrayList<A>(rest.length + 1);
        list.add(first);
        list.addAll(Arrays.asList(rest));
        return from(entity, list);
    }
    
    public <A> SubType from(Expr<A> entity, Iterable<A> col) {
        alias(entity, col);
        query.from((Expr<?>)entity);
        return _this;
    }
         
    @SuppressWarnings("unchecked")
    public CloseableIterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : move this code to querydsl-core
        final Expr<?>[] full = asArray(new Expr[rest.length + 2], e1, e2, rest);
        boolean oneType = true;
        if (e1.getType().isAssignableFrom((e2.getType()))){
            for (Expr<?> e : rest){
                if (!e1.getType().isAssignableFrom(e.getType())){
                    oneType = false;
                }
            }
        }else{
            oneType = false;
        }
        Class<?> type = e1.getType();
        if (!oneType){
            type = Object.class;    
        }  
        return iterate(new Expr.EArrayConstructor(type, full));
    }    
    
    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        return wrap(query.iterate(projection));
    }
    // alias variant
    public <RT> CloseableIterator<RT> iterate(RT alias) {
        return iterate(MiniApi.getAny(alias));
    }
    
    public long count(){
        try {
            return query.count();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * NOTE : use iterate for huge projections
     * 
     * @param e1
     * @param e2
     * @param rest
     * @return
     */
    public List<Object[]> list(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        try{
            ArrayList<Object[]> rv = new ArrayList<Object[]>();
            CloseableIterator<Object[]> it = iterate(e1, e2, rest);
            while (it.hasNext()){
                rv.add(it.next());
            }
            return rv;    
        }finally{
            close();
        }        
    }
    
    /**
     * NOTE : use iterate for huge projections
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    public <RT> List<RT> list(Expr<RT> projection) {        
        try {
            ArrayList<RT> rv = new ArrayList<RT>();
            Iterator<RT> it = query.iterate(projection);
            while (it.hasNext()){
                rv.add(it.next());
            }
            return rv;
        }finally{
            close();
        }            
    }
    // alias variant
    public <RT> List<RT> list(RT alias) {
        return list(MiniApi.getAny(alias));
    }
    
    public SubType orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return _this;
    }   
    
    public <RT> RT uniqueResult(Expr<RT> expr) {        
        try{
            Iterator<RT> it = query.iterate(expr);
            return it.hasNext() ? it.next() : null;
        }finally{
            close();
        }        
    }
    // alias variant
    public <RT> RT uniqueResult(RT alias) {
        return uniqueResult(MiniApi.getAny(alias));
    }
        
    public SubType where(Expr.EBoolean... o) {
        query.where(o);
        return _this;
    }
    // alias variant
    public SubType where(boolean alias){
        return where( MiniApi.$(alias));
    }
    
    public void setSortSources(boolean s){
        this.sortSources = s;
    }
    
    public void setSourceSortingSupport(SourceSortingSupport sourceSortingSupport) {
        this.sourceSortingSupport = sourceSortingSupport;
    }

    public void setWrapIterators(boolean w){
        this.wrapIterators = w;
    }
    
    public void setSequentialUnion(boolean sequentialUnion) {
        this.sequentialUnion = sequentialUnion;
    }
    
    private <T> CloseableIterator<T> wrap(final Iterator<T> it){
        return new CloseableIterator<T>(){
            public boolean hasNext() {
                return it.hasNext();
            }
            public T next() {
                return it.next();
            }
            public void remove() {
                it.remove();                
            }
            public void close() throws IOException {
                AbstractColQuery.this.close();                
            }            
        };
    }
    
    public class InnerQuery extends QueryBase<Object, InnerQuery> {
        
        private <RT> Iterator<RT> createIterator(Expr<RT> projection) throws Exception {
            List<Expr<?>> sources = new ArrayList<Expr<?>>();
            // from  / where       
            Iterator<?> it;
            if (joins.size() == 1){
                it = handleFromWhereSingleSource(sources);
            }else{
                it = handleFromAndWhere(sources);   
            }

            if (it.hasNext()){
                // order
                if (!orderBy.isEmpty()){
                    it = handleOrderBy(sources, it);
                }
                
                // select    
                return wrap(handleSelect(it, sources, projection));
                
            }else{
                return wrap(Collections.<RT>emptyList().iterator());
            }
                               
        }
        
        public long count() throws Exception {
            List<Expr<?>> sources = new ArrayList<Expr<?>>();
            Iterator<?> it;
            if (joins.size() == 1){
                it = handleFromWhereSingleSource(sources);
            }else{
                it = handleFromAndWhere(sources);   
            }
            long count = 0l;
            while (it.hasNext()){
                it.next();
                count++;
            }
            return count;
        }

        protected Iterator<?> handleFromAndWhere(List<Expr<?>> sources) throws Exception{
            EBoolean condition = where.create();
            if (sortSources){               
                sourceSortingSupport.sortSources(joins, condition);               
            }
            for (JoinExpression<?> join : joins) {
                sources.add(join.getTarget());
            }
            indexSupport = createIndexSupport(exprToIt, ops, sources);
            
            if (sequentialUnion && condition instanceof Operation && ((Operation<?,?>)condition).getOperator() == Ops.OR){
                // TODO : handle deeper OR operations as well
                Operation<?,?> op = (Operation<?,?>)condition;
                IteratorChain<Object[]> chain = new IteratorChain<Object[]>();
                EBoolean e1 = (EBoolean)op.getArgs()[0], e2 = (EBoolean)op.getArgs()[1];
                chain.addIterator(createMultiIterator(sources, e1));
                chain.addIterator(createMultiIterator(sources, e2.and(e1.not())));
                return chain;
            }else{
                return createMultiIterator(sources, condition);
            }            
        }
        
        protected Iterator<?> handleFromWhereSingleSource(List<Expr<?>> sources) throws Exception{
            EBoolean condition = where.create();
            JoinExpression<?> join = joins.get(0);
            sources.add(join.getTarget());
            indexSupport = createIndexSupport(exprToIt, ops, sources);            
            // create a simple projecting iterator for Object -> Object[]
            
            if (sequentialUnion && condition instanceof Operation && ((Operation<?,?>)condition).getOperator() == Ops.OR){
                Operation<?,?> op = (Operation<?,?>)condition;
                IteratorChain<Object[]> chain = new IteratorChain<Object[]>();
                EBoolean e1 = (EBoolean)op.getArgs()[0], e2 = (EBoolean)op.getArgs()[1];
                Iterator<?> it1 = indexSupport.getChildFor(e1).getIterator(join.getTarget());
                chain.addIterator(multiArgFilter(ops, toArrayIterator(it1), sources, e1));
                Iterator<?> it2 = indexSupport.getChildFor(e2.and(e1.not())).getIterator(join.getTarget());
                chain.addIterator(multiArgFilter(ops, toArrayIterator(it2), sources, e2.and(e1.not())));
                return chain;
            }else{
                Iterator<?> it = toArrayIterator(indexSupport.getChildFor(condition).getIterator(join.getTarget()));
                if (condition != null){
                    // wrap the iterator if a where constraint is available
                    it = multiArgFilter(ops, it, sources, condition);
                }
                return it;    
            }           
            
        }

        private Iterator<Object[]> createMultiIterator(List<Expr<?>> sources, EBoolean condition) {
            MultiIterator multiIt;
            if (condition == null || !wrapIterators){
                multiIt = new MultiIterator();
            }else{
                multiIt = new FilteringMultiIterator(ops, condition);                
            }        
            for (Expr<?> expr : sources) multiIt.add(expr);
            multiIt.init(indexSupport.getChildFor(condition));
            
            if (condition != null){
                return multiArgFilter(ops, multiIt, sources, condition);
            }else{
                return multiIt;
            }
        }
        
        @SuppressWarnings("unchecked")
        protected Iterator<?> handleOrderBy(List<Expr<?>> sources, Iterator<?> it)
                throws Exception {
            // create a projection for the order
            Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
            boolean[] directions = new boolean[orderBy.size()];
            for (int i = 0; i < orderBy.size(); i++){
                orderByExpr[i] = (Expr)orderBy.get(i).target;
                directions[i] = orderBy.get(i).order == Order.ASC;
            }
            Expr<?> expr = new Expr.EArrayConstructor<Object>(Object.class, orderByExpr);
            Evaluator ev = EvaluatorUtils.create(ops, sources, expr);
            
            // transform the iterator to list
            List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>)it);               
            Collections.sort(itAsList, new MultiComparator(ev, directions));
            it = itAsList.iterator();
            return it;
        }

        protected <RT> Iterator<RT> handleSelect(Iterator<?> it, List<Expr<?>> sources, Expr<RT> projection) throws Exception {
            return transform(ops, it, sources, projection);
        }

        public <RT> Iterator<RT> iterate(final Expr<RT> projection) {
            select(projection);
            try {
                return createIterator(projection);
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
        }
    }





}
