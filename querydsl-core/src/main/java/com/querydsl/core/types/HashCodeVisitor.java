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
 * {@code HashCodeVisitor} is used for hashCode generation in {@link Expression} implementations.
 *
 * @author tiwe
 */
public final class HashCodeVisitor implements Visitor<Integer,Void> {

    public static final HashCodeVisitor DEFAULT = new HashCodeVisitor();

    private HashCodeVisitor() { }

    @Override
    public Integer visit(Constant<?> expr, Void context) {
        return expr.getConstant().hashCode();
    }

    @Override
    public Integer visit(FactoryExpression<?> expr, Void context) {
        int result = expr.getType().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

    @Override
    public Integer visit(Operation<?> expr, Void context) {
        int result = expr.getOperator().name().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

    @Override
    public Integer visit(ParamExpression<?> expr, Void context) {
        return expr.getName().hashCode();
    }

    @Override
    public Integer visit(Path<?> expr, Void context) {
        return expr.getMetadata().hashCode();
    }

    @Override
    public Integer visit(SubQueryExpression<?> expr, Void context) {
        return expr.getMetadata().hashCode();
    }

    @Override
    public Integer visit(TemplateExpression<?> expr, Void context) {
        int result = expr.getTemplate().hashCode();
        return 31 * result + expr.getArgs().hashCode();
    }

}
