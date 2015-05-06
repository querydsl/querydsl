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
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.mysema.query.sql.types.BlobType;
import com.mysema.query.sql.types.BooleanType;
import com.mysema.query.sql.types.ByteType;
import com.mysema.query.sql.types.CharacterType;
import com.mysema.query.sql.types.DoubleType;
import com.mysema.query.sql.types.FloatType;
import com.mysema.query.sql.types.InputStreamType;
import com.mysema.query.sql.types.IntegerType;
import com.mysema.query.sql.types.LongType;
import com.mysema.query.sql.types.ObjectType;
import com.mysema.query.sql.types.ShortType;

public class JavaTypeMappingTest {

    private JavaTypeMapping typeMapping = new JavaTypeMapping();
    
    @Test
    public void GetType_With_Subtypes() {        
        typeMapping.register(new InputStreamType());
        assertNotNull(typeMapping.getType(InputStream.class));
        assertNotNull(typeMapping.getType(FileInputStream.class));
    }
    
    @Test
    public void GetType_With_Interfaces() {
        assertEquals(BlobType.class, typeMapping.getType(DummyBlob.class).getClass());
    }
    
    @Test
    public void GetType_for_Object() {
        assertEquals(ObjectType.class, typeMapping.getType(Object.class).getClass());
    }
    
    @Test
    public void GetType_For_Primitive() {
        assertEquals(ByteType.class, typeMapping.getType(byte.class).getClass());
        assertEquals(ShortType.class, typeMapping.getType(short.class).getClass());
        assertEquals(IntegerType.class, typeMapping.getType(int.class).getClass());
        assertEquals(LongType.class, typeMapping.getType(long.class).getClass());
        assertEquals(FloatType.class, typeMapping.getType(float.class).getClass());
        assertEquals(DoubleType.class, typeMapping.getType(double.class).getClass());
        assertEquals(BooleanType.class, typeMapping.getType(boolean.class).getClass());
        assertEquals(CharacterType.class, typeMapping.getType(char.class).getClass());
    }

}
