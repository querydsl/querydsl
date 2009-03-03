/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.apache.commons.collections15.IteratorUtils;
import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.collections.comparators.JoinExpressionComparator;
import com.mysema.query.collections.comparators.MultiComparator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.collections.iterators.MultiArgFilteringIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.iterators.ProjectingIterator;
import com.mysema.query.collections.iterators.WrappingIterator;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;

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
 * @author tiwe
 * @version $Id$
 */
public class AbstractColQuery<SubType extends AbstractColQuery<SubType>> {
        
    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType)this;
    
    private final Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();
    
    private IteratorFactory iteratorFactory;

    private final JavaOps ops;
    
    private final InnerQuery query = new InnerQuery();
    
    private boolean sortSources = true, wrapIterators = true;

    public AbstractColQuery() {
        this(JavaOps.DEFAULT);
    }
    
    public AbstractColQuery(JavaOps ops) {
        this.ops = ops;
        this.iteratorFactory = new DefaultIteratorFactory(exprToIt);
    }
    
    public AbstractColQuery(IteratorFactory iteratorFactory){
        this(JavaOps.DEFAULT, iteratorFactory);
    }
    
    public AbstractColQuery(JavaOps ops, IteratorFactory iteratorFactory) {
        this.ops = ops;
        this.iteratorFactory = iteratorFactory;
    }
    
    public <A> SubType alias(Expr<A> path, Iterable<A> col) {
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
         
    public Iterable<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
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
    
    public <RT> Iterable<RT> iterate(Expr<RT> projection) {
        return query.iterate(projection);
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
        ArrayList<Object[]> rv = new ArrayList<Object[]>();
        for (Object[] v : iterate(e1, e2, rest)){
            rv.add(v);
        }
        return rv;
    }
    
    /**
     * NOTE : use iterate for huge projections
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    public <RT> List<RT> list(Expr<RT> projection) {
        ArrayList<RT> rv = new ArrayList<RT>();
        for (RT v : query.iterate(projection)){
            rv.add(v);
        }
        return rv;
    }
    
    public SubType orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return _this;
    }
        
    public void setSortSources(boolean s){
        this.sortSources = s;
    }
    
    public void setWrapIterators(boolean w){
        this.wrapIterators = true;
    }
    
    public <RT> RT uniqueResult(Expr<RT> expr) {
        Iterator<RT> it = query.iterate(expr).iterator();
        return it.hasNext() ? it.next() : null;
    }
    
    public SubType where(Expr.EBoolean... o) {
        query.where(o);
        return _this;
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
                    
            // order
            if (!orderBy.isEmpty()) it = handleOrderBy(sources, it);
            
            // select
            return handleSelect(it, sources, projection);                   
        }
        
        protected Iterator<?> handleFromAndWhere(List<Expr<?>> sources) throws Exception{
            MultiIterator multiIt;
            if (where.create() == null || !wrapIterators){
                // cartesian view
                multiIt = new MultiIterator();
            }else{
                // filtered cartesian view
                multiIt = new FilteringMultiIterator(ops, where.create());
                if (sortSources){                    
                    JoinExpressionComparator comp = new JoinExpressionComparator(where.create(), exprToIt.keySet());
                    if (joins.size() == 2){
                        if (comp.comparePrioritiesOnly(joins.get(0), joins.get(1)) > 0){
                            JoinExpression<Object> je = joins.set(0, joins.get(1));
                            joins.set(1, je);
                        }                         
                    }else{
                        Collections.sort(joins, comp);
                    }                    
                }
            }        
            for (JoinExpression<?> join : joins) {
                sources.add(join.getTarget());
                multiIt.add(join.getTarget());
                switch(join.getType()){
                case JOIN :       
                case INNERJOIN :  // TODO
                case LEFTJOIN :   // TODO
                case FULLJOIN :   // TODO
                case DEFAULT :    // do nothing
                }
            }   
            iteratorFactory.init(sources, where.create());
            multiIt.init(iteratorFactory);
            
            if (!wrapIterators && (where.create() != null)){
                ExpressionEvaluator ev = new JavaSerializer(ops).handle(where.create())
                    .createExpressionEvaluator(sources, boolean.class);
                return new MultiArgFilteringIterator<Object>(multiIt, ev);    
            }else{
                return multiIt;    
            }            
        }
        
        protected Iterator<?> handleFromWhereSingleSource(List<Expr<?>> sources) throws Exception{
            JoinExpression<?> join = joins.get(0);
            sources.add(join.getTarget());
            iteratorFactory.init(sources, where.create());
            
            // create a simple projecting iterator for Object -> Object[]
            Iterator<?> it = new WrappingIterator<Object[]>(iteratorFactory.getIterator(join.getTarget())){
               public Object[] next() {
                   return new Object[]{nextFromOrig()};
               }               
            };
            
            if (where.create() != null){
                // wrap the iterator if a where constraint is available
                ExpressionEvaluator ev = new JavaSerializer(ops).handle(
                        where.create()).createExpressionEvaluator(sources,
                        boolean.class);
                it = new MultiArgFilteringIterator<Object>(it, ev);    
            }
            return it;
        }

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
            ExpressionEvaluator ev = new JavaSerializer(ops).handle(expr)
                .createExpressionEvaluator(sources, expr);
            
            // transform the iterator to list
            List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>)it);               
            Collections.sort(itAsList, new MultiComparator(ev, directions));
            it = itAsList.iterator();
            return it;
        }

        protected <RT> Iterator<RT> handleSelect(Iterator<?> it, List<Expr<?>> sources, Expr<RT> projection) throws Exception {
            ExpressionEvaluator ev = new JavaSerializer(ops).handle(projection)
                .createExpressionEvaluator(sources,projection);        
            return new ProjectingIterator<RT>(it, ev);
        }

        public <RT> Iterable<RT> iterate(final Expr<RT> projection) {
            select(projection);
            return new Iterable<RT>() {
                public Iterator<RT> iterator() {
                    try {
                        return createIterator(projection);
                    } catch (Exception e) {
                        throw new RuntimeException("error", e);
                    }
                }
            };
        }
    }

}
