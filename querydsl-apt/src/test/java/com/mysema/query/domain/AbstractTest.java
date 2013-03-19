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
package com.mysema.query.domain;

import static org.junit.Assert.*;
import junit.framework.Assert;

public abstract class AbstractTest {

    public Class<?> cl;

    protected void match(Class<?> expectedType, String name) throws SecurityException, NoSuchFieldException {
        assertTrue(cl.getSimpleName()+"."+name + " failed", expectedType.isAssignableFrom(cl.getField(name).getType()));
    }

    protected void assertMissing(String name) {
        try {
            cl.getField(name);
            Assert.fail("Expected missing field : " + cl.getSimpleName() + "." + name);
        } catch (NoSuchFieldException e) {
            // expected
        }
    }

}
