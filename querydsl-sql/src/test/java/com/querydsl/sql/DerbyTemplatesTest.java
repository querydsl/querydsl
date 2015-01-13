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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;

public class DerbyTemplatesTest {

    @Test
    public void NextVal() {
        Operation<String> nextval = OperationImpl.create(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("next value for myseq", new SQLSerializer(new Configuration(new DerbyTemplates())).handle(nextval).toString());
    }

}
