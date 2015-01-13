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

import com.querydsl.core.domain.QCat;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.path.BooleanPath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class ToStringVisitorTest {
    
    private Templates templates = new Templates() {
        {
            add(PathType.PROPERTY, "{0}_{1}");
            add(PathType.COLLECTION_ANY, "{0}");
        }};
    
    @Test
    public void Operation() {
        assertEquals("cat_name is not null", 
                QCat.cat.name.isNotNull().accept(ToStringVisitor.DEFAULT, templates));
    }
        
    @Test
    public void Template() {
        Expression<Boolean> template = TemplateExpressionImpl.create(Boolean.class, "{0} is not null", QCat.cat.name);
        assertEquals("cat_name is not null", 
                template.accept(ToStringVisitor.DEFAULT, templates));
    }
    
    @Test
    public void Path() {
        assertEquals("cat_kittens_kittens_name", 
                QCat.cat.kittens.any().kittens.any().name.accept(ToStringVisitor.DEFAULT, templates));
    }

    @Test
    public void Complex() {
        BooleanPath a = Expressions.booleanPath("a");
        BooleanPath b = Expressions.booleanPath("d");
        BooleanPath c = Expressions.booleanPath("c");
        BooleanPath d = Expressions.booleanPath("d");
        Predicate complex = a.or(b).and(c.or(d));
        assertEquals("(a || d) && (c || d)", complex.accept(ToStringVisitor.DEFAULT, templates));


    }

}
