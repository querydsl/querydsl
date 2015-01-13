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

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Test;

import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.apt.domain.QSignatureTest_APropertyChangeSupported;
import com.querydsl.apt.domain.QSignatureTest_AValueObject;
import com.querydsl.core.types.path.ComparablePath;

public class SignatureTest {
    
    @QuerySupertype
    public static abstract class APropertyChangeSupported implements Comparable<Object>, Cloneable, Serializable {
        
    }

    @QuerySupertype
    public static abstract class AValueObject extends APropertyChangeSupported implements Comparable<Object>, Cloneable, Serializable {
        
    }
    
    @Test
    public void APropertyChangeSupported() {
        assertEquals(ComparablePath.class, QSignatureTest_APropertyChangeSupported.class.getSuperclass());
    }
    
    @Test
    public void AValueObject() {
        assertEquals(ComparablePath.class, QSignatureTest_AValueObject.class.getSuperclass());
    }

}
