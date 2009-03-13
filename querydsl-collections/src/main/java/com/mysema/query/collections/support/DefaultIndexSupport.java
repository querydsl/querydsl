package com.mysema.query.collections.support;

import java.util.*;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.collections.IndexSupport;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.collections.utils.QueryIteratorUtils;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.util.Assert;

/**
 * ExtendedIndexSupport provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexSupport implements IndexSupport{
    
    private JavaOps ops;
    
    private List<? extends Expr<?>> sources;
    
    private Map<Expr<?>,Iterable<?>> exprToIt;
    
    Map<Path<?>,Map<?,? extends Iterable<?>>> pathEqPathIndex;
    
    Map<Path<?>,Evaluator> pathToIndexKey;
    
    private long fullSize = 0l;
    
    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        if (pathToIndexKey.containsKey(expr)){
            Evaluator ev = pathToIndexKey.get(expr);
            Map<?,? extends Iterable<?>> indexEntry = pathEqPathIndex.get(expr);
            Object key = ev.evaluate(bindings);
            if (indexEntry.containsKey(key)){
                return (Iterator<A>)indexEntry.get(key).iterator();    
            }else{
                return Collections.<A>emptyList().iterator();
            }            
        }else{
            return (Iterator<A>)exprToIt.get(expr).iterator();    
        }        
    }

    public void init(Map<Expr<?>,Iterable<?>> exprToIt, JavaOps ops, List<? extends Expr<?>> sources, EBoolean condition) {
        this.exprToIt = exprToIt;
        this.ops = Assert.notNull(ops);
        this.sources = Assert.notNull(sources);        
        this.exprToIt = Assert.notNull(exprToIt);        
        this.pathEqPathIndex = new HashMap<Path<?>,Map<?,? extends Iterable<?>>>();
        this.pathToIndexKey = new HashMap<Path<?>,Evaluator>();
        
        for (Iterable<?> value : exprToIt.values()){
            if (value instanceof Collection) fullSize += ((Collection<?>)value).size();
        }
        
        // populate the "path eq path" index
        if (condition instanceof Operation){
            visitOperation((Operation<?,?>) condition);
        }
        
    }

    private void visitOperation(Operation<?,?> op) {
        if (op.getOperator() == Ops.EQ_OBJECT  || op.getOperator() == Ops.EQ_PRIMITIVE){
            if (op.getArgs()[0] instanceof Path && op.getArgs()[1] instanceof Path){
                Path<?> p1 = (Path<?>) op.getArgs()[0], p2 = (Path<?>) op.getArgs()[1];
                int i1 = sources.indexOf(p1.getRoot());
                int i2 = sources.indexOf(p2.getRoot());
                if (i1 < i2){
                    indexPath(p2, p1);
                }else if (i1 > i2){
                    indexPath(p1, p2);
                }
            }            
            
        }else if (op.getOperator() == Ops.NOT){    
            // skip negative condition paths
            
        }else if (op.getOperator() == Ops.AND || op.getOperator() == Ops.OR){
            for (Expr<?> e : op.getArgs()){
                if (e instanceof Operation) visitOperation((Operation<?, ?>) e);
            }
        }        
    }
    
    private void indexPath(Path<?> path, Path<?> key) {
        if (!pathEqPathIndex.containsKey(path.getRoot())){
            // create the index entry
            Evaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)path);
            Map<?,? extends Iterable<?>> map = QueryIteratorUtils.projectToMap(exprToIt.get(path.getRoot()).iterator(), ev);
            
            // create the key creator            
            Evaluator keyCreator = EvaluatorUtils.create(ops, sources, (Expr<?>)key);
            
            // update the index
            pathEqPathIndex.put(path.getRoot(), map);   
            pathToIndexKey.put(path.getRoot(), keyCreator);            
        }
    }

}
