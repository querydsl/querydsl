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
package com.querydsl.jpa;

import org.junit.Test;

import com.querydsl.core.domain.QCat;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.expr.NumberOperation;
import com.querydsl.core.types.expr.StringOperation;
import com.querydsl.core.types.path.StringPath;

public class StringOperationsTest extends AbstractQueryTest{

    @Test
    public void StringConcatenations() {
        assertToString("concat(cat.name,kitten.name)", cat.name.concat(kitten.name));
    }

    @Test
    public void StringConversionOperations() {
        assertToString("str(cat.bodyWeight)", cat.bodyWeight.stringValue());
    }

    @Test
    public void StringOperationsInFunctionalWay() {
        assertToString("concat(cat.name,cust.name.firstName)", cat.name.concat(cust.name.firstName));
        assertToString("lower(cat.name)", cat.name.lower());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void IndexOf() {        
        Path path = QCat.cat.name;
        Expression startIndex = Expressions.constant(0);
        Expression endIndex = NumberOperation.create(Integer.class, Ops.INDEX_OF, path, Expressions.constant("x"));
        Expression substr = StringOperation.create(Ops.SUBSTR_2ARGS, path, startIndex, endIndex);
        assertToString("substring(cat.name,1,locate(?1,cat.name)-1)", substr);
    }
    
    @Test
    public void IndexOf2() {
        StringPath str = QCat.cat.name;
        assertToString("substring(cat.name,1,locate(?1,cat.name)-1)", str.substring(0, str.indexOf("x")));
    }
    
    @Test
    public void IndexOf3() {
        assertToString("substring(cat.name,2,1)", QCat.cat.name.substring(1,2));
    }
}
