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
package com.querydsl.codegen;

import java.io.Serializable;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;

public class Embedded2Test extends AbstractExporterTest {
    
    @QuerySupertype
    public static class EntityCode {

        public String code;

    }
    
    @QuerySupertype
    public static abstract class AbstractEntity<C extends EntityCode> {

        @QueryEmbedded
        public C code;
        
    }
    
    @QuerySupertype
    public static class AbstractMultilingualEntity<C extends EntityCode> extends AbstractEntity<C> {

    }
    
    @QuerySupertype
    public static abstract class AbstractNamedEntity<C extends EntityCode> extends AbstractMultilingualEntity<C> {

        public String nameEn;

        public String nameNl;

    }
        
    @QueryEntity
    public static class Brand extends AbstractNamedEntity<BrandCode> {

        public Long id;

    }
        
    public interface Entity<T> extends Serializable {

        boolean sameIdentityAs(T other);

    }
     
    @QueryEmbeddable
    public static class BrandCode extends EntityCode {

    }
    

}
