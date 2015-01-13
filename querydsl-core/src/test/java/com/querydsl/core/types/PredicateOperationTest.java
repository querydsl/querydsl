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

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PredicateOperationTest {

    @SuppressWarnings("unchecked")
    @Test
    public void Not() {
        Path o1 = new PathImpl(Object.class, "o1");
        Path o2 = new PathImpl(Object.class, "o2");
        PredicateOperation template = PredicateOperation.create(Ops.EQ, o1, o2);
        assertEquals("!(o1 = o2)", template.not().toString());
    }

}
