/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.core.types;

import java.util.List;

/**
 * TemplateExpression provides base types for custom expressions with integrated
 * serialization templates
 *
 * @author tiwe
 */
public interface TemplateExpression<T> extends Expression<T> {

    /**
     * Get the argument with the given index
     *
     * @param index
     * @return
     */
    Object getArg(int index);

    /**
     * Get the arguments of the custom expression
     *
     * @return
     */
    List<?> getArgs();

    /**
     * Get the serialization template for this custom expression
     *
     * @return
     */
    Template getTemplate();

}
