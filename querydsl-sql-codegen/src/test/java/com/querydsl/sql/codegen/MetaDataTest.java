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
package com.querydsl.sql.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.querydsl.codegen.EntityType;

public class MetaDataTest {
    
    private EntityType classModel;
    
    @Before
    public void setUp() {
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        String packageName = "com.myproject.domain";
        String tableName = "vwServiceName";
        String className = namingStrategy.getClassName(tableName);
        
        Type classTypeModel = new SimpleType(TypeCategory.ENTITY, packageName + "." + className, packageName, className, false, false);
        classModel = new EntityType(classTypeModel);
//        classModel.addAnnotation(new TableImpl(namingStrategy.normalizeTableName(tableName)));
        classModel.getData().put("table", namingStrategy.normalizeTableName(tableName));
    }

    @Test
    public void GetSimpleName() {                
        assertEquals("VwServiceName", classModel.getSimpleName());        
    }
    
    @Test
    public void GetFullName() {
        assertEquals("com.myproject.domain.VwServiceName", classModel.getFullName());
    }
    
}
