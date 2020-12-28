/*
 * Copyright 2010, Mysema Ltd
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
package com.querydsl.codegen.utils;

import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.Type;

import java.util.Map;

/**
 *
 * @author pgrant
 */
public interface EvaluatorFactory {

    /**
     * @param source
     * @param projectionType
     * @param names
     * @param classes
     * @param constants
     * @return
     */
    <T> Evaluator<T> createEvaluator(String source, Class<? extends T> projectionType, 
            String[] names, Class<?>[] classes, Map<String, Object> constants);

    /**
     * Create a new Evaluator instance
     *
     * @param <T>
     * projection type
     * @param source
     * expression in Java source code form
     * @param projection
     * type of the source expression
     * @param names
     * names of the arguments
     * @param types
     * types of the arguments
     * @param constants
     * @return
     */
    <T> Evaluator<T> createEvaluator(String source, ClassType projection, String[] names,
                                     Type[] types, Class<?>[] classes, Map<String, Object> constants);
    
}
