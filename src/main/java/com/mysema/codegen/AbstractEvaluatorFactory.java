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
package com.mysema.codegen;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;

/**
 * @author tiwe
 *
 */
public abstract class AbstractEvaluatorFactory implements EvaluatorFactory{
    
    private final Map<String, Class<?>> cache = new WeakHashMap<String, Class<?>>();
    
    protected ClassLoader loader;
    
    /**
     * @param source
     * @param projection
     * @param names
     * @param types
     * @param id
     * @param constants
     * @throws IOException
     */
    protected abstract void compile(String source, ClassType projection, String[] names, Type[] types,
            String id, Map<String, Object> constants) throws IOException;

    
    @Override
    public <T> Evaluator<T> createEvaluator(String source, Class<? extends T> projectionType,
            String[] names, Class<?>[] classes, Map<String, Object> constants) {
        Type[] types = new Type[classes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = new ClassType(TypeCategory.SIMPLE, classes[i]);
        }
        return createEvaluator(source, new ClassType(TypeCategory.SIMPLE, projectionType), names,
                types, classes, constants);
    }


    /**
     * Create a new Evaluator instance
     * 
     * @param <T>
     *            projection type
     * @param source
     *            expression in Java source code form
     * @param projection
     *            type of the source expression
     * @param names
     *            names of the arguments
     * @param types
     *            types of the arguments
     * @param constants
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Evaluator<T> createEvaluator(String source, ClassType projection, String[] names,
            Type[] types, Class<?>[] classes, Map<String, Object> constants) {
        try {
            String id = toId(source, projection.getJavaClass(), types, constants.values());
            Class<?> clazz = cache.get(id);
                        
            if (clazz == null) {
                try {
                    clazz = loader.loadClass(id);
                } catch (ClassNotFoundException e) {
                    compile(source, projection, names, types, id, constants);
                    // reload
                    clazz = loader.loadClass(id);
                }
                cache.put(id, clazz);
            }
            
            Object object = !constants.isEmpty() ? clazz.newInstance() : null;
            
            // TODO : improve this
            for (Map.Entry<String, Object> entry : constants.entrySet()) {
                Field field = clazz.getField(entry.getKey());
                field.set(object, entry.getValue());
            }

            Method method = clazz.getMethod("eval", classes);
            return new MethodEvaluator<T>(method, object, (Class) projection.getJavaClass());
        } catch (ClassNotFoundException e) {
            throw new CodegenException(e);
        } catch (SecurityException e) {
            throw new CodegenException(e);
        } catch (NoSuchMethodException e) {
            throw new CodegenException(e);
        } catch (NoSuchFieldException e) {
            throw new CodegenException(e);
        } catch (InstantiationException e) {
            throw new CodegenException(e);
        } catch (IOException e) {
            throw new CodegenException(e);
        } catch (IllegalAccessException e) {
            throw new CodegenException(e);
        }
    }

    protected String toId(String source, Class<?> returnType, Type[] types, Collection<Object> constants) {
        StringBuilder b = new StringBuilder(128);
        b.append("Q");
        b.append("_").append(source.hashCode());
        b.append("_").append(returnType.getName().hashCode());
        for (Type type : types) {
            b.append("_").append(type.getFullName().hashCode());
        }
        for (Object constant : constants) {
            b.append("_").append(constant.getClass().getName().hashCode());
        }
        return b.toString().replace('-', '0');
    }
    
}
