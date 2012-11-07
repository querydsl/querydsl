package com.mysema.query.codegen;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.mysema.codegen.model.Type;


public class GenericTest {
    
    public static abstract class CapiBCKeyedByGrundstueck {
        
    }
    
    public static abstract class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {
        
    }

    public static abstract class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends
            CapiBCKeyedByGrundstueck {
    }
    
    private TypeFactory typeFactory = new TypeFactory();
    
    @Test
    public void HidaBez() {
        Type type = typeFactory.createEntityType(HidaBez.class);
        System.out.println(type.getGenericName(true));
    }
    
    @Test
    public void HidaBezGruppe() {
        Type type = typeFactory.createEntityType(HidaBezGruppe.class);
        System.out.println(type.getGenericName(true));
    }

}
