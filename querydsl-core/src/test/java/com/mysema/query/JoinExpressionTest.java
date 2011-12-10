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
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class JoinExpressionTest {

    private JoinExpression je = new JoinExpression(JoinType.DEFAULT, new StringPath("str"));

    @Test
    public void ToString() {
        assertEquals("DEFAULT str", je.toString());
    }

    @Test
    public void AddCondition() {
        je.addCondition(BooleanConstant.TRUE);
        assertEquals(BooleanConstant.TRUE, je.getCondition());

        je.addCondition(BooleanConstant.FALSE);
        assertEquals(BooleanConstant.TRUE.and(BooleanConstant.FALSE), je.getCondition());
    }

    @Test
    public void AddFlag() {
        JoinFlag x = new JoinFlag("x");
        JoinFlag y = new JoinFlag("y");
        assertFalse(je.hasFlag(x));

        je.addFlag(x);
        assertTrue(je.hasFlag(x));
        assertFalse(je.hasFlag(y));
        
        je.addFlag(y);
        assertTrue(je.hasFlag(y));
    }

}
