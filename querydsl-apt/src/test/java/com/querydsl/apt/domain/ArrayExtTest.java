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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QArrayExtTest_BinaryFile;
import com.querydsl.core.types.path.ArrayPath;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.MapPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;

public class ArrayExtTest {

    private static final QArrayExtTest_BinaryFile binaryFile = QArrayExtTest_BinaryFile.binaryFile;
    
    @QueryEntity
    public static class BinaryFile {

        byte[] contentPart;
        
        List<byte[]> list;
        
        Map<String, byte[]> map1;
        
        Map<byte[], String> map2;
    }

    @Test
    public void BinaryFile_contentPart() {
        assertEquals(ArrayPath.class, binaryFile.contentPart.getClass());
        assertEquals(byte[].class, binaryFile.contentPart.getType());
    }
    
    @Test
    public void BinaryFile_list() {
        assertEquals(ListPath.class, binaryFile.list.getClass());
        assertEquals(List.class, binaryFile.list.getType());        
        assertEquals(byte[].class, binaryFile.list.getParameter(0));
        
        assertEquals(SimplePath.class, binaryFile.list.get(0).getClass());
        assertEquals(byte[].class, binaryFile.list.get(0).getType());
    }
    
    @Test
    public void BinaryFile_map1() {
        assertEquals(MapPath.class, binaryFile.map1.getClass());
        assertEquals(Map.class, binaryFile.map1.getType());
        assertEquals(String.class, binaryFile.map1.getParameter(0));
        assertEquals(byte[].class, binaryFile.map1.getParameter(1));
        
        assertEquals(SimplePath.class, binaryFile.map1.get("").getClass());
        assertEquals(byte[].class, binaryFile.map1.get("").getType());
    }
    
    @Test
    public void BinaryFile_map2() {
        assertEquals(MapPath.class, binaryFile.map2.getClass());
        assertEquals(Map.class, binaryFile.map2.getType());
        assertEquals(byte[].class, binaryFile.map2.getParameter(0));
        assertEquals(String.class, binaryFile.map2.getParameter(1));
        
        assertEquals(StringPath.class, binaryFile.map2.get(new byte[0]).getClass());
        assertEquals(String.class, binaryFile.map2.get(new byte[0]).getType());
    }
    
}
