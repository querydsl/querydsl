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
package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.domain.QCat;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.TemplateExpressionImpl;


public class ListAccessVisitorTest {

    private final QCat cat = QCat.cat;
    
    @Test
    public void Path() {
        assertEquals("cat_kittens_0", serialize(cat.kittens.get(0)));
    }
    
    @Test
    public void Longer_Path() {
        assertEquals("cat_kittens_0.name", serialize(cat.kittens.get(0).name));
    }
    
    @Test
    public void Very_Long_Path() {
        assertEquals("cat_kittens_0_kittens_1.name", serialize(cat.kittens.get(0).kittens.get(1).name));
    }
    
    @Test
    public void Simple_BooleanOperation() {        
        Predicate predicate = cat.kittens.get(0).name.eq("Ruth123");        
        assertEquals("cat_kittens_0.name = Ruth123", serialize(predicate));
    }
    
    @Test
    public void Simple_StringOperation() {        
        Predicate predicate = cat.kittens.get(0).name.substring(1).eq("uth123");        
        assertEquals("substring(cat_kittens_0.name,1) = uth123", serialize(predicate));
    }
    
    @Test
    public void And_Operation() {
        Predicate predicate = cat.kittens.get(0).name.eq("Ruth123").and(cat.kittens.get(1).bodyWeight.gt(10.0));
        assertEquals("cat_kittens_0.name = Ruth123 && cat_kittens_1.bodyWeight > 10.0", serialize(predicate));
    }
    
    @Test
    public void Template() {
        Expression<Boolean> templateExpr = TemplateExpressionImpl.create(Boolean.class, "{0} = {1}", 
                cat.kittens.get(0).name, ConstantImpl.create("Ruth123"));
        assertEquals("cat_kittens_0.name = Ruth123", serialize(templateExpr));
    }
    
    private String serialize(Expression<?> expression) {
        ListAccessVisitor visitor = new ListAccessVisitor() {
            @Override
            protected Predicate exists(Context c, Predicate condition) {
                return condition;
            }            
        };
        Expression<?> transformed = expression.accept(visitor, new Context());
        return transformed.toString();
    }
    
}
