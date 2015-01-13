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

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.apt.domain.QEmbeddableInterfaceTest_EmbeddableClass;
import com.querydsl.apt.domain.QEmbeddableInterfaceTest_EmbeddableInterface;
import com.querydsl.apt.domain.QEmbeddableInterfaceTest_EntityClass;

public class EmbeddableInterfaceTest {

    @Entity
    public static class EntityClass {
        
        @ElementCollection(targetClass=EmbeddableClass.class)
        Collection<EmbeddableInterface> children;
        
    }
        
    @Embeddable
    public interface EmbeddableInterface {
     
        String getName();
    }
    
    @Embeddable
    public static class EmbeddableClass implements EmbeddableInterface {

        @Override
        public String getName() {
            return null;
        }
        
    }
    
    @Test
    public void Type() {
        Assert.assertEquals(
                QEmbeddableInterfaceTest_EmbeddableInterface.class,
                QEmbeddableInterfaceTest_EntityClass.entityClass.children.any().getClass());
    }
    
    @Test
    public void Properties() {
        assertNotNull(QEmbeddableInterfaceTest_EmbeddableInterface.embeddableInterface.name);
        assertNotNull(QEmbeddableInterfaceTest_EmbeddableClass.embeddableClass.name);
    }
    
}
