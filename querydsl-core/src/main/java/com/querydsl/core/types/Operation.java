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
package com.querydsl.core.types;

import java.util.List;

/**
 * {@code Operation} represents an operation with operator and arguments
 *
 * @author tiwe
 * @param <T> expression type
 */
public interface Operation<T> extends Expression<T> {

    /**
     * Get the argument with the given index
     *
     * @param index zero based index of expression
     * @return expression at index
     */
    Expression<?> getArg(int index);

    /**
     * Get the arguments of this operation
     *
     * @return arguments
     */
    List<Expression<?>> getArgs();

    /**
     * Get the operator symbol for this operation
     *
     * @return operator
     */
    Operator getOperator();

}
