package com.querydsl.codegen;

import org.junit.Test;

import com.querydsl.codegen.utils.model.Type;


public class GenericTest {

    public abstract static class CapiBCKeyedByGrundstueck {

    }

    public abstract static class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    }

    public abstract static class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends
            CapiBCKeyedByGrundstueck {
    }

    private TypeFactory typeFactory = new TypeFactory();

    @Test
    public void hidaBez() {
        Type type = typeFactory.getEntityType(HidaBez.class);
        //System.out.println(type.getGenericName(true));
    }

    @Test
    public void hidaBezGruppe() {
        Type type = typeFactory.getEntityType(HidaBezGruppe.class);
        //System.out.println(type.getGenericName(true));
    }

}
