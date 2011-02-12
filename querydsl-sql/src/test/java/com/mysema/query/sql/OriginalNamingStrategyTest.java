package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.EntityType;

public class OriginalNamingStrategyTest {

    private NamingStrategy namingStrategy = new OriginalNamingStrategy();
    
    private EntityType entityModel = new EntityType("Q", Types.OBJECT);
    
    @Test
    public void GetClassName() {
        assertEquals("Quser_data", namingStrategy.getClassName("Q", "", "user_data"));
        assertEquals("Qu", namingStrategy.getClassName("Q", "", "u"));
        assertEquals("Qus",namingStrategy.getClassName("Q", "", "us"));
        assertEquals("Qu_", namingStrategy.getClassName("Q", "", "u_"));
        assertEquals("Qus_",namingStrategy.getClassName("Q", "", "us_"));
    }
    
    @Test
    public void GetClassName_with_Suffix() {
        assertEquals("user_dataType", namingStrategy.getClassName("", "Type", "user_data"));
        assertEquals("uType", namingStrategy.getClassName("", "Type", "u"));
        assertEquals("usType",namingStrategy.getClassName("", "Type", "us"));
        assertEquals("u_Type", namingStrategy.getClassName("", "Type", "u_"));
        assertEquals("us_Type",namingStrategy.getClassName("", "Type", "us_"));
    }


    @Test
    public void GetPropertyName() {
        assertEquals("while_col", namingStrategy.getPropertyName("while", "Q", "", entityModel));
        assertEquals("name", namingStrategy.getPropertyName("name", "Q", "", entityModel));
        assertEquals("user_id", namingStrategy.getPropertyName("user_id", "Q", "", entityModel));
        assertEquals("accountEvent_id", namingStrategy.getPropertyName("accountEvent_id", "Q", "", entityModel));
    }

    @Test
    public void GetPropertyNameForInverseForeignKey(){
        assertEquals("_fk_superior", namingStrategy.getPropertyNameForInverseForeignKey("fk_superior", entityModel));
    }
    
    @Test
    public void GetPropertyNameForForeignKey(){
        assertEquals("fk_superior", namingStrategy.getPropertyNameForForeignKey("fk_superior", entityModel));
        assertEquals("FK_SUPERIOR", namingStrategy.getPropertyNameForForeignKey("FK_SUPERIOR", entityModel));        
    }
    
    @Test
    public void GetDefaultVariableName(){
        assertEquals("object", namingStrategy.getDefaultVariableName("Q", "", entityModel));
    }
}
