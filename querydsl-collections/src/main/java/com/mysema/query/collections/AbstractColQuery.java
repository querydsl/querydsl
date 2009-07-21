package com.mysema.query.collections;

import static com.mysema.query.collections.utils.QueryIteratorUtils.multiArgFilter;
import static com.mysema.query.collections.utils.QueryIteratorUtils.toArrayIterator;
import static com.mysema.query.collections.utils.QueryIteratorUtils.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.IteratorChain;
import org.apache.commons.collections15.iterators.UniqueFilterIterator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.alias.Alias;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.collections.iterators.LimitingIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.support.DefaultIndexSupport;
import com.mysema.query.collections.support.DefaultSourceSortingSupport;
import com.mysema.query.collections.support.MultiComparator;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.support.QueryBaseWithProjectionAndDetach;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operation;
import com.mysema.query.types.operation.Ops;

//TODO : implement leftJoin, rightJoin and fullJoin
//TODO : implement groupBy and having
/**
 * AbstractColQuery provides a base class for Collection query implementations.
 * Extend it like this
 * 
 * <pre>
 * public class MyType extends AbstractColQuery&lt;MyType&gt;{
 *   ...
 * }
 * </pre>
 * 
 * @see ColQuery
 * 
 * @author tiwe
 * @version $Id$
 */
public class AbstractColQuery<SubType extends AbstractColQuery<SubType>> extends QueryBaseWithProjectionAndDetach<SubType> implements ColQuery {
    
    private boolean arrayProjection = false;

    private final Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();

    private QueryIndexSupport indexSupport;

    private final ColQueryPatterns patterns;

    /**
     * turn OR queries into sequential UNION queries
     */
    private boolean sequentialUnion = false;

    /**
     * optimize sort order for optimal index usage
     */
    private boolean sortSources = true;

    private SourceSortingSupport sourceSortingSupport;

    /**
     * wrap single source iterators to avoid cartesian view
     */
    private boolean wrapIterators = true;

    public AbstractColQuery() {
        this(ColQueryPatterns.DEFAULT);
    }

    public AbstractColQuery(ColQueryPatterns patterns) {
        this.patterns = patterns;
        this.sourceSortingSupport = new DefaultSourceSortingSupport();
    }

    public AbstractColQuery(QueryMetadata metadata) {
        super(metadata);
        this.patterns = ColQueryPatterns.DEFAULT;
        this.sourceSortingSupport = new DefaultSourceSortingSupport();
    }

    protected <A> SubType alias(Expr<A> path, Iterable<? extends A> col) {
        exprToIt.put(path, col);
        return _this;
    }

    @SuppressWarnings("unchecked")
    private <RT> Iterator<RT> asDistinctIterator(Iterator<RT> rv) {
        if (!arrayProjection) {
            return new UniqueFilterIterator<RT>(rv);
        } else {
            return new FilterIterator<RT>(rv, new Predicate() {
                private Set<List<Object>> set = new HashSet<List<Object>>();

                public boolean evaluate(Object object) {
                    return set.add(Arrays.asList((Object[]) object));
                }
            });
        }
    }

    private boolean changeToUnionQuery(EBoolean condition) {
        return sequentialUnion && condition instanceof Operation
                && ((Operation<?, ?>) condition).getOperator() == Ops.OR;
    }

    public long count() {
        try {
            List<Expr<?>> sources = new ArrayList<Expr<?>>();
            Iterator<?> it;
            if (getMetadata().getJoins().size() == 1) {
                it = handleFromWhereSingleSource(sources);
            } else {
                it = handleFromWhereMultiSource(sources);
            }
            if (getMetadata().isDistinct()) {
                arrayProjection = true;
                it = asDistinctIterator(it);
            }
            long count = 0l;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            return count;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected QueryIndexSupport createIndexSupport(
            Map<Expr<?>, Iterable<?>> exprToIt, ColQueryPatterns patterns,
            List<Expr<?>> sources) {
        return new DefaultIndexSupport(new SimpleIteratorSource(exprToIt), patterns, sources);
    }

    private <RT> Iterator<RT> createIterator(Expr<RT> projection) throws Exception {
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        // from / where
        Iterator<?> it;
        if (getMetadata().getJoins().size() == 1) {
            it = handleFromWhereSingleSource(sources);
        } else {
            it = handleFromWhereMultiSource(sources);
        }

        if (it.hasNext()) {
            // order
            if (!getMetadata().getOrderBy().isEmpty()) {
                it = handleOrderBy(sources, it);
            }

            // select
            return handleSelect(it, sources, projection);

        } else {
            return Collections.<RT> emptyList().iterator();
        }

    }

    private Iterator<Object[]> createMultiIterator(List<Expr<?>> sources, EBoolean condition) {
        MultiIterator multiIt;
        if (condition == null || !wrapIterators) {
            multiIt = new MultiIterator();
        } else {
            multiIt = new FilteringMultiIterator(patterns, condition);
        }
        for (Expr<?> expr : sources)
            multiIt.add(expr);
        multiIt.init(indexSupport.getChildFor(condition));

        if (condition != null) {
            return multiArgFilter(patterns, multiIt, sources, condition);
        } else {
            return multiIt;
        }
    }

    private <RT> Iterator<RT> createPagedIterator(Expr<RT> projection) throws Exception {
        Iterator<RT> iterator = createIterator(projection);
        return LimitingIterator.transform(iterator, getMetadata()
                .getModifiers());
    }

    public <A> SubType from(Expr<A> entity, A first, A... rest) {
        List<A> list = new ArrayList<A>(rest.length + 1);
        list.add(first);
        list.addAll(Arrays.asList(rest));
        return from(entity, list);
    }

    public <A> SubType from(Expr<A> entity, Iterable<? extends A> col) {
        alias(entity, col);
        from((Expr<?>) entity);
        return _this;
    }

    protected Iterator<?> handleFromWhereMultiSource(List<Expr<?>> sources) throws Exception {
        EBoolean condition = getMetadata().getWhere();
        List<JoinExpression> joins = new ArrayList<JoinExpression>(getMetadata().getJoins());
        if (sortSources) {
            sourceSortingSupport.sortSources(joins, condition);
        }
        for (JoinExpression join : joins) {
            sources.add(join.getTarget());
        }
        indexSupport = createIndexSupport(exprToIt, patterns, sources);

        if (changeToUnionQuery(condition)) {
            // TODO : handle deeper OR operations as well
            Operation<?, ?> op = (Operation<?, ?>) condition;

            IteratorChain<Object[]> chain = new IteratorChain<Object[]>();
            EBoolean e1 = (EBoolean) op.getArg(0), e2 = (EBoolean) op.getArg(1);
            chain.addIterator(createMultiIterator(sources, e1));
            chain.addIterator(createMultiIterator(sources, e2.and(e1.not())));
            return chain;
        } else {
            return createMultiIterator(sources, condition);
        }
    }

    protected Iterator<?> handleFromWhereSingleSource(List<Expr<?>> sources) throws Exception {
        EBoolean condition = getMetadata().getWhere();
        JoinExpression join = getMetadata().getJoins().get(0);
        sources.add(join.getTarget());
        indexSupport = createIndexSupport(exprToIt, patterns, sources);
        // create a simple projecting iterator for Object -> Object[]

        if (changeToUnionQuery(condition)) {
            Operation<?, ?> op = (Operation<?, ?>) condition;

            IteratorChain<Object[]> chain = new IteratorChain<Object[]>();
            EBoolean e1 = (EBoolean) op.getArg(0), e2 = (EBoolean) op.getArg(1);
            Iterator<?> it1 = indexSupport.getChildFor(e1).getIterator(join.getTarget());
            chain.addIterator(multiArgFilter(patterns, toArrayIterator(it1),sources, e1));
            Iterator<?> it2 = indexSupport.getChildFor(e2.and(e1.not())).getIterator(join.getTarget());
            chain.addIterator(multiArgFilter(patterns, toArrayIterator(it2),sources, e2.and(e1.not())));
            return chain;
        } else {
            Iterator<?> it = toArrayIterator(indexSupport.getChildFor(condition).getIterator(join.getTarget()));
            if (condition != null) {
                // wrap the iterator if a where constraint is available
                it = multiArgFilter(patterns, it, sources, condition);
            }
            return it;
        }

    }

    @SuppressWarnings("unchecked")
    protected Iterator<?> handleOrderBy(List<Expr<?>> sources, Iterator<?> it) throws Exception {
        // create a projection for the order
        List<OrderSpecifier<?>> orderBy = getMetadata().getOrderBy();
        Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            orderByExpr[i] = (Expr) orderBy.get(i).getTarget();
            directions[i] = orderBy.get(i).getOrder() == Order.ASC;
        }
        Expr<?> expr = new EArrayConstructor<Object>(Object.class, orderByExpr);
        Evaluator ev = EvaluatorUtils.create(patterns, sources, expr);

        // transform the iterator to list
        List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>) it);
        Collections.sort(itAsList, new MultiComparator(ev, directions));
        it = itAsList.iterator();
        return it;
    }

    protected <RT> Iterator<RT> handleSelect(Iterator<?> it,
            List<Expr<?>> sources, Expr<RT> projection) throws Exception {
        Iterator<RT> rv = transform(patterns, it, sources, projection);
        if (getMetadata().isDistinct()) {
            rv = asDistinctIterator(rv);
        }
        return rv;
    }

    @SuppressWarnings("unchecked")
    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        arrayProjection = true;
        Expr<?>[] full = asArray(new Expr[rest.length + 2], first, second, rest);
        Expr<Object[]> projection = new EArrayConstructor(Object.class, full);
        addToProjection(projection);
        try {
            return createPagedIterator(projection);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        addToProjection(projection);
        try {
            return createPagedIterator(projection);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <RT> List<RT> list(RT projection) {
        return list(Alias.getAny(projection));
    }

    // TODO : optimize
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        QueryModifiers modifiers = getMetadata().getModifiers();
        List<RT> list;
        try {
            list = IteratorUtils.toList(createIterator(projection));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (list.isEmpty()) {
            return SearchResults.emptyResults();
        } else if (!modifiers.isRestricting()) {
            return new SearchResults<RT>(list, modifiers, list.size());
        } else {
            int start = 0;
            int end = list.size();
            if (modifiers.getOffset() != null) {
                if (modifiers.getOffset() < list.size()) {
                    start = modifiers.getOffset().intValue();
                } else {
                    return new SearchResults<RT>(Collections.<RT> emptyList(),
                            modifiers, list.size());
                }
            }
            if (modifiers.getLimit() != null) {
                end = (int) Math.min(start + modifiers.getLimit(), list.size());
            }
            return new SearchResults<RT>(list.subList(start, end), modifiers,
                    list.size());
        }
    }

    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return super.uniqueResult(first, second, rest);
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        return super.uniqueResult(expr);
    }

    public void setSequentialUnion(boolean sequentialUnion) {
        this.sequentialUnion = sequentialUnion;
    }

    public void setSortSources(boolean s) {
        this.sortSources = s;
    }

    public void setSourceSortingSupport(SourceSortingSupport sourceSortingSupport) {
        this.sourceSortingSupport = sourceSortingSupport;
    }

    public void setWrapIterators(boolean w) {
        this.wrapIterators = w;
    }

}
