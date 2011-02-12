package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.query.codegen.EntityType;

public class MetaDataTest {
    
    private EntityType classModel;
    
    @Before
    public void setUp(){
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        String packageName = "com.myproject.domain";
        String tableName = "vwServiceName";
        String namePrefix = "Q";
        String className = namingStrategy.getClassName(namePrefix, "", tableName);
        
        Type classTypeModel = new SimpleType(TypeCategory.ENTITY,
                packageName + "." + className, packageName, className, false, false);
        classModel = new EntityType(namePrefix, "", classTypeModel);
        classModel.addAnnotation(new TableImpl(namingStrategy.normalizeTableName(tableName)));        
    }

    @Test
    public void GetSimpleName(){                
        assertEquals("QVwServiceName", classModel.getSimpleName());        
    }
    
    @Test
    public void GetFullName(){
        assertEquals("com.myproject.domain.QVwServiceName", classModel.getFullName());
    }
    
}
