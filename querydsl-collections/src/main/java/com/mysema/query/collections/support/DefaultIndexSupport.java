package com.mysema.query.collections.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.collections.IndexSupport;
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
    
//    private EBoolean condition;
    
    private final Map<Expr<?>,Iterable<?>> exprToIt;
    
    final Map<Path<?>,Map<?,? extends Iterable<?>>> pathEqPathIndex;
    
    public DefaultIndexSupport(Map<Expr<?>,Iterable<?>> exprToIt){
        this.exprToIt = Assert.notNull(exprToIt);
        this.pathEqPathIndex = new HashMap<Path<?>,Map<?,? extends Iterable<?>>>();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        // TODO : make use of the index when appropriate
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    public void init(JavaOps ops, List<? extends Expr<?>> sources, EBoolean condition) {
        this.ops = Assert.notNull(ops);
        this.sources = Assert.notNull(sources);
//        this.condition = Assert.notNull(condition);
        
        // populate the "path eq path" index
        // TODO : make a decision when index usage is appropriate
        if (condition instanceof Operation){
            visitOperation((Operation<?,?>) condition);
        }
        
        // TODO : filter the sources, based on non-contextual parts of the condition
    }

    private void visitOperation(Operation<?,?> op) {
        if (op.getOperator() == Ops.EQ_OBJECT  || op.getOperator() == Ops.EQ_PRIMITIVE){
            if (op.getArgs()[0] instanceof Path && op.getArgs()[1] instanceof Path){
                Path<?> p1 = (Path<?>) op.getArgs()[0], p2 = (Path<?>) op.getArgs()[1];
                int i1 = sources.indexOf(p1.getRoot());
                int i2 = sources.indexOf(p2.getRoot());
                if (i1 < i2){
                    indexPath(p2);
                }else if (i1 > i2){
                    indexPath(p1);
                }
            }            
        }else{
            for (Expr<?> e : op.getArgs()){
                if (e instanceof Operation) visitOperation((Operation<?, ?>) e);
            }
        }        
    }
    
    private void indexPath(Path<?> path) {
        ExpressionEvaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)path);
        Map<?,? extends Iterable<?>> map = QueryIteratorUtils.projectToMap(exprToIt.get(path.getRoot()).iterator(), ev);
        pathEqPathIndex.put(path, map);        
    }

}
