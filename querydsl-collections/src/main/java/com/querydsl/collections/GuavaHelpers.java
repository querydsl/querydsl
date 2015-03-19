/*
 * Copyright 2012, Mysema Ltd
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
package com.querydsl.collections;

import java.util.Collections;

import com.google.common.base.Function;
import com.mysema.codegen.Evaluator;
import com.querydsl.core.EmptyMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathExtractor;
import com.querydsl.core.types.Predicate;

/**
 * GuavaHelpers provides functionality to wrap Querydsl {@link Predicate} instances to Guava predicates
 * and Querydsl {@link Expression} instances to Guava functions 
 * 
 * @author tiwe
 *
 */
public final class GuavaHelpers {
    
    private static final DefaultEvaluatorFactory evaluatorFactory = 
            new DefaultEvaluatorFactory(CollQueryTemplates.DEFAULT);
    
    /**
     * Wrap a Querydsl predicate into a Guava predicate
     * 
     * @param predicate
     * @return
     */
    public static <T> com.google.common.base.Predicate<T> wrap(Predicate predicate) {        
        Path<?> path = predicate.accept(PathExtractor.DEFAULT, null);
        if (path != null) {
            final Evaluator<Boolean> ev = createEvaluator(path.getRoot(), predicate);
            return new com.google.common.base.Predicate<T>() {
                @Override
                public boolean apply(T input) {
                    return ev.evaluate(input);
                }                
            };
        } else {
            throw new IllegalArgumentException("No path in " + predicate);
        }
    }
    
    /**
     * Wrap a Querydsl expression into a Guava function
     * 
     * @param projection
     * @return
     */
    public static <F,T> Function<F,T> wrap(Expression<T> projection) {        
        Path<?> path = projection.accept(PathExtractor.DEFAULT, null);
        if (path != null) {
            final Evaluator<T> ev = createEvaluator(path.getRoot(), projection);
            return new Function<F,T>() {
                @Override
                public T apply(F input) {
                    return ev.evaluate(input);
                }                
            };
        } else {
            throw new IllegalArgumentException("No path in " + projection);
        }
    }
    
    private static <F,T> Evaluator<T> createEvaluator(Path<F> path, Expression<T> projection) {
        return evaluatorFactory.create(EmptyMetadata.DEFAULT, 
                Collections.singletonList(path), projection);
    }
    
    private GuavaHelpers() {  }

}
