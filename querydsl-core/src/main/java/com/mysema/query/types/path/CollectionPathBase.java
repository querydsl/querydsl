package com.mysema.query.types.path;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.expr.CollectionExpressionBase;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * CollectionPath is a base class for collection typed paths
 * 
 * @author tiwe
 *
 * @param <C>
 * @param <E>
 */
public abstract class CollectionPathBase<C extends Collection<E>, E> extends CollectionExpressionBase<C,E> implements Path<C>{

    private static final long serialVersionUID = -9004995667633601298L;

    public CollectionPathBase(Class<? extends C> type) {
        super(type);
    }

    @Nullable
    private transient volatile Constructor<?> constructor;
    
    public abstract SimpleExpression<E> any();
    
    @SuppressWarnings("unchecked")
    protected <Q extends SimpleExpression<E>> Q newInstance(Class<Q> queryType, PathMetadata<?> pm) throws NoSuchMethodException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException {
        if (constructor == null) {
            if (Constants.isTyped(queryType)){
                constructor = queryType.getConstructor(Class.class, PathMetadata.class);
            }else{
                constructor = queryType.getConstructor(PathMetadata.class);
            }
        }
        if (Constants.isTyped(queryType)){
            return (Q)constructor.newInstance(getElementType(), pm);
        }else{
            return (Q)constructor.newInstance(pm);
        }
    }

}
