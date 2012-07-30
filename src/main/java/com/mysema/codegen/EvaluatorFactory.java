/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mysema.codegen;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import java.util.Map;

/**
 *
 * @author pgrant
 */
public interface EvaluatorFactory {

    <T> Evaluator<T> createEvaluator(String source, Class<? extends T> projectionType, String[] names, Class<?>[] classes, Map<String, Object> constants);

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
    @SuppressWarnings(value = "unchecked")
    <T> Evaluator<T> createEvaluator(String source, ClassType projection, String[] names, Type[] types, Class<?>[] classes, Map<String, Object> constants);
    
}
