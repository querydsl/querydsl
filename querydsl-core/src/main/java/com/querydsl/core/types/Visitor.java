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

/**
 * {@code Visitor} defines a visitor signature for {@link Expression} instances.
 *
 * @author tiwe
 *
 * @param <R> Return type
 * @param <C> Context type
 */
public interface Visitor<R, C> {

    /**
     * Visit a Constant instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Constant<?> expr, C context);

    /**
     * Visit a FactoryExpression instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(FactoryExpression<?> expr, C context);

    /**
     * Visit an Operation instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Operation<?> expr, C context);

    /**
     * Visit a ParamExpression instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(ParamExpression<?> expr, C context);

    /**
     * Visit a Path instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(Path<?> expr, C context);

    /**
     * Visit a SubQueryExpression instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(SubQueryExpression<?> expr, C context);

    /**
     * Visit a TemplateExpression instance with the given context
     *
     * @param expr expression to visit
     * @param context context of the visit or null, if not used
     * @return visit result
     */
    R visit(TemplateExpression<?> expr, C context);

}
