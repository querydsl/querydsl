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
package com.querydsl.core.types.path;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Operation;

public class NumberPathTest {

    private NumberPath<Byte> bytePath = new NumberPath<Byte>(Byte.class, "bytePath");

    @SuppressWarnings("unchecked")
    @Test
    public void BytePath_in() {
        Operation<?> operation = (Operation<?>) bytePath.in(1, 2, 3);

        List<Byte> numbers = (List<Byte>) ((Constant)operation.getArg(1)).getConstant();
        assertEquals(Byte.valueOf((byte)1), numbers.get(0));
        assertEquals(Byte.valueOf((byte)2), numbers.get(1));
        assertEquals(Byte.valueOf((byte)3), numbers.get(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void BytePath_notIn() {
        Operation<?> operation = (Operation<?>) bytePath.notIn(1, 2, 3);

        List<Byte> numbers = (List<Byte>) ((Constant)operation.getArg(1)).getConstant();
        assertEquals(Byte.valueOf((byte)1), numbers.get(0));
        assertEquals(Byte.valueOf((byte)2), numbers.get(1));
        assertEquals(Byte.valueOf((byte)3), numbers.get(2));
    }
}
