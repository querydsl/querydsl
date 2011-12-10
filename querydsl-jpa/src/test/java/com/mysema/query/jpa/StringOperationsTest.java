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
package com.mysema.query.jpa;

import org.junit.Test;

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

}
