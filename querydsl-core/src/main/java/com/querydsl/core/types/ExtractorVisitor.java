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


/**
 * ExtractorVisitor is a Visitor implementation for Expression extraction
 * 
 * @author tiwe
 *
 */
final class ExtractorVisitor implements Visitor<Expression<?>,Void> {
    
    public static final ExtractorVisitor DEFAULT = new ExtractorVisitor();
    
    private ExtractorVisitor() {}
    
    @Override
    public Expression<?> visit(Constant<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Operation<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(ParamExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Path<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(SubQueryExpression<?> expr, Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(TemplateExpression<?> expr, Void context) {
        return expr;
    }
    
}
