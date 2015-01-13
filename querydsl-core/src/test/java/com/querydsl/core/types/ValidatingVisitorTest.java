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

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.template.SimpleTemplate;

@SuppressWarnings("unchecked")
public class ValidatingVisitorTest {

    private final Set<Expression<?>> known = new HashSet<Expression<?>>();

    private final ValidatingVisitor validator = ValidatingVisitor.DEFAULT;

    @Before
    public void setUp() {
        known.add(new PathImpl(Object.class, "path"));
    }

    @Test
    public void VisitConstantOfQVoid() {
        validator.visit(ConstantImpl.create("XXX"), known);
    }

    @Test
    public void VisitFactoryExpressionOfQVoid() {
        validator.visit(new QBean(Object.class, new PathImpl(String.class, "path")), known);
    }

    @Test
    public void VisitOperationOfQVoid() {
        validator.visit((Operation)new SimplePath(Object.class, "path").isNull(), known);
    }

    @Test
    public void VisitParamExpressionOfQVoid() {
        validator.visit(new Param(Object.class, "prop"), known);
    }

    @Test
    public void VisitPathOfQVoid() {
        validator.visit(new PathImpl(Object.class, "path"), known);
    }

    @Test
    public void VisitSubQueryExpressionOfQVoid() {
        validator.visit(new SubQueryExpressionImpl(Object.class, new DefaultQueryMetadata()), known);
    }

    @Test
    public void VisitTemplateExpressionOfQVoid() {
        validator.visit((TemplateExpression)SimpleTemplate.create(Object.class, "XXX"), known);
    }

}
