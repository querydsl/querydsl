/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.dsl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.annotation.Nullable;

import com.querydsl.core.types.ExpressionException;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;

/**
 * {@code CollectionPath} is a base class for collection typed paths
 *
 * @author tiwe
 *
 * @param <C> collection type
 * @param <E> component type
 * @param <Q> expression type for {@code any()} results
 */
public abstract class CollectionPathBase<C extends Collection<E>, E, Q extends SimpleExpression<? super E>>
        extends CollectionExpressionBase<C, E> implements Path<C> {

    private static final long serialVersionUID = -9004995667633601298L;

    @Nullable
    private transient volatile Constructor<?> constructor;

    private transient volatile boolean usePathInits = false;

    private final PathInits inits;

    public CollectionPathBase(PathImpl<C> mixin, PathInits inits) {
        super(mixin);
        this.inits = inits;
    }

    /**
     * Create a path that matches any element in this collection
     *
     * @return path expression
     */
    public abstract Q any();

    @SuppressWarnings("unchecked")
    protected Q newInstance(Class<Q> queryType, PathMetadata pm) {
        try {
            if (constructor == null) {
                if (Constants.isTyped(queryType)) {
                    try {
                        constructor = queryType.getDeclaredConstructor(Class.class, PathMetadata.class, PathInits.class);
                        usePathInits = true;
                    } catch (NoSuchMethodException e) {
                        constructor = queryType.getDeclaredConstructor(Class.class, PathMetadata.class);
                    }
                } else {
                    try {
                        constructor = queryType.getDeclaredConstructor(PathMetadata.class, PathInits.class);
                        usePathInits = true;
                    } catch (NoSuchMethodException e) {
                        constructor = queryType.getDeclaredConstructor(PathMetadata.class);
                    }
                }
                constructor.setAccessible(true);
            }
            if (Constants.isTyped(queryType)) {
                if (usePathInits) {
                    return (Q) constructor.newInstance(getElementType(), pm, inits);
                } else {
                    return (Q) constructor.newInstance(getElementType(), pm);
                }

            } else {
                if (usePathInits) {
                    return (Q) constructor.newInstance(pm, inits);
                } else {
                    return (Q) constructor.newInstance(pm);
                }
            }
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e);
        } catch (InstantiationException e) {
            throw new ExpressionException(e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e);
        }

    }

}
