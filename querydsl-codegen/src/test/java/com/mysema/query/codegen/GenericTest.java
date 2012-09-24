package com.mysema.query.codegen;

import org.junit.Test;

import com.mysema.codegen.model.Type;


public class GenericTest {
    
    public static abstract class CapiBCKeyedByGrundstueck {
        
    }
    
    public static abstract class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {
        
    }

    public static abstract class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends
            CapiBCKeyedByGrundstueck {
    }
    
    @Test
    public void HidaBez() {
        TypeFactory typeFactory = new TypeFactory();
        Type type = typeFactory.createEntityType(HidaBez.class);
        System.out.println(type.getGenericName(true));
    }
    
    @Test
    public void HidaBezGruppe() {
        TypeFactory typeFactory = new TypeFactory();
        Type type = typeFactory.createEntityType(HidaBezGruppe.class);
        System.out.println(type.getGenericName(true));
    }

}
