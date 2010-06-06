package com.mysema.query.collections.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.QueryEngine;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class DefaultQueryEngine implements QueryEngine {

    private final DefaultEvaluatorFactory evaluatorFactory;
    
    public DefaultQueryEngine(DefaultEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }
    
    @Override
    public long count(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables){
        if (metadata.getJoins().size() == 1){
            return evaluateSingleSource(metadata, iterables, true).size();
        }else{
            return evaluateMultipleSources(metadata, iterables, true).size();
        }
    }
    
    private List<?> distinct(List<?> list, boolean array) {
        if (array){
            Set set = new HashSet();
            List rv = new ArrayList();
            for (Object o : list){
                if (set.add(Arrays.asList((Object[])o))){
                    rv.add(o);
                }
            }            
            return rv;            
        }else{
            return new ArrayList(new HashSet(list));
        }
    }

    private List evaluateMultipleSources(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables, boolean count) {
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        for (JoinExpression join : metadata.getJoins()){
            if (join.getType() == JoinType.DEFAULT){
                sources.add(join.getTarget());    
            }else{
                Operation target = (Operation) join.getTarget();
               sources.add(target.getArg(1));
            }            
        }
        
        // from where
        Evaluator<List<Object[]>> ev = evaluatorFactory.createEvaluator(metadata.getJoins(), metadata.getWhere());
        List<Iterable<?>> iterableList = new ArrayList<Iterable<?>>(metadata.getJoins().size());
        for (JoinExpression join : metadata.getJoins()){
            if (join.getType() == JoinType.DEFAULT){
                iterableList.add(iterables.get(join.getTarget()));
            }
        }
        List<?> list = ev.evaluate(iterableList.toArray());
        
        if (!count && !list.isEmpty()){            
            // ordered
            if (!metadata.getOrderBy().isEmpty()){
                order(metadata, sources, list);
            }
            
            // limit + offset
            if (metadata.getModifiers().isRestricting()){
                list = metadata.getModifiers().subList(list);
            }
            
            // projection
            list = project(metadata, sources, list);                
        }
        
        // distinct
        if (metadata.isDistinct()){
            boolean array = !list.isEmpty() ? list.get(0).getClass().isArray() : false;            
            list = distinct(list, array);
        }                
        
        return list;
    }


    private List evaluateSingleSource(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables, boolean count) {        
        Expr<?> source = metadata.getJoins().get(0).getTarget();
        Iterable<?> iterable = iterables.values().iterator().next(); 
        List<?> list;
        if (iterable instanceof List){
            list = (List)iterable;
        }else{
            list = IteratorAdapter.asList(iterable.iterator());
        }
        
        // from & where
        if (metadata.getWhere() != null){
            Evaluator<List<?>> evaluator = (Evaluator)evaluatorFactory.createEvaluator(source, metadata.getWhere());
            list = evaluator.evaluate(list);
        }        
        
        if (!count && !list.isEmpty()){
            // clone list
            list = new ArrayList(list);
            
            // ordered
            if (!metadata.getOrderBy().isEmpty()){
                order(metadata, Collections.<Expr<?>>singletonList(source), list);
            }        
            
            // limit + offset
            if (metadata.getModifiers().isRestricting()){
                list = metadata.getModifiers().subList(list);
            }
            
            // projection
            if (metadata.getProjection().size() > 1 || !metadata.getProjection().get(0).equals(source)){
                list = project(metadata, Collections.<Expr<?>>singletonList(source), list);
            }            
        
        }
        

        // distinct
        if (metadata.isDistinct()){
            boolean array = !list.isEmpty() ? list.get(0).getClass().isArray() : false;
            list = distinct(list, array);
        }
        
        return list;
        
    }

    @Override
    public List<?> list(QueryMetadata metadata, Map<Expr<?>, Iterable<?>> iterables){        
        if (metadata.getJoins().size() == 1){
            return evaluateSingleSource(metadata, iterables, false);
        }else{
            return evaluateMultipleSources(metadata, iterables, false);
        }        
    }
    
    private void order(QueryMetadata metadata, List<Expr<?>> sources, List<?> list) {
        // create a projection for the order
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            orderByExpr[i] = (Expr) orderBy.get(i).getTarget();
            directions[i] = orderBy.get(i).getOrder() == Order.ASC;
        }
        Expr<?> expr = new EArrayConstructor<Object>(Object[].class, orderByExpr);
        Evaluator orderEvaluator = evaluatorFactory.create(sources, expr);

        Collections.sort(list, new MultiComparator(orderEvaluator, directions));
    }


    private List<?> project(QueryMetadata metadata, List<Expr<?>> sources, List<?> list) {
        Evaluator projectionEvaluator = evaluatorFactory.create(sources, metadata.getProjection().get(0));
        EvaluatorTransformer transformer = new EvaluatorTransformer(projectionEvaluator);
        list = IteratorUtils.toList(IteratorUtils.transformedIterator(list.iterator(), transformer));
        return list;
    }
    
}
