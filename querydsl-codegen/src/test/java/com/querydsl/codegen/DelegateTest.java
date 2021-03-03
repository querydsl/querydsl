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
package com.querydsl.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Test;

import com.querydsl.codegen.utils.model.Parameter;
import com.querydsl.codegen.utils.model.Types;

public class DelegateTest {

    @Test
    public void equals_object() {
        Delegate delegate = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        Delegate delegate2 = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        assertEquals(delegate, delegate2);
    }

    @Test
    public void not_equals_object() {
        Delegate delegate = new Delegate(Types.STRING, Types.STRING, "delegate", Collections.<Parameter>emptyList(), Types.STRING);
        Delegate delegate2 = new Delegate(Types.STRING, Types.STRING, "delegate2", Collections.<Parameter>emptyList(), Types.STRING);
        assertFalse(delegate.equals(delegate2));
    }

}
