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
package com.querydsl.core.alias;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MethodTypeTest {

    @Test
    public void Get() throws SecurityException, NoSuchMethodException {
        Method getVal = MethodTypeTest.class.getMethod("getVal");
        Method hashCode = Object.class.getMethod("hashCode");
        Method size = Collection.class.getMethod("size");
        Method toString = Object.class.getMethod("toString");

        assertEquals(MethodType.GET_MAPPED_PATH, MethodType.get(ManagedObject.class.getMethod("__mappedPath")));
        assertEquals(MethodType.GETTER, MethodType.get(getVal));
        assertEquals(MethodType.HASH_CODE, MethodType.get(hashCode));
        assertEquals(MethodType.LIST_ACCESS, MethodType.get(List.class.getMethod("get", int.class)));
        assertEquals(MethodType.MAP_ACCESS, MethodType.get(Map.class.getMethod("get", Object.class)));
        assertEquals(MethodType.SIZE, MethodType.get(size));
        assertEquals(MethodType.TO_STRING, MethodType.get(toString));

    }

    public String getVal() {
        return "";
    }
}
