package com.mysema.query.collections;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathExtractor;
import com.mysema.query.grammar.types.PathMetadata;

/**
 * MiniApi provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApi {
    
    private static PathMetadata<String> pm = PathMetadata.forVariable("it");
    
    public static Path<Object> it = new Path.Entity<Object>(Object.class,"it");
    
    public static Path.Comparable<Byte> byteVal = createVar(Byte.class);
    public static Path.Comparable<Integer> intVal = createVar(Integer.class);    
    public static Path.Comparable<Long> longVal = createVar(Long.class);
    public static Path.Comparable<Float> floatVal = createVar(Float.class);
    public static Path.Comparable<Double> doubleVal = createVar(Double.class);
    
    public static Path.String stringVal = new Path.String(pm), str = stringVal;
    
    @SuppressWarnings("unchecked")
    public static <A> Iterable<A> select(final Iterable<A> from, Expr.Boolean where){
        Path<?> path = new PathExtractor().handle(where).getPath();
        final ColQuery query = new ColQuery().from(path, from).where(where);
        return query.iterate((Expr<A>)path);
    }
        
    public static <A> Iterable<A> reject(final Iterable<A> from, Expr.Boolean where){
        return select(from, Grammar.not(where));
    }

    private static <A extends Comparable<A>> Path.Comparable<A> createVar(Class<A> cl){
        return new Path.Comparable<A>(cl, pm);
    }
    
    
}
