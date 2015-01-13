package com.querydsl.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.Test;

import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeExtends;
import com.querydsl.core.annotations.QueryEntity;

public class Inheritance2Test {

    @QueryEntity
    public abstract class Base<T extends Base<T>> {
        @SuppressWarnings("unchecked")
        Base2 base;
        Base2<?,?> base2;
    }

    @QueryEntity
    public abstract class Base2<T extends Base2<T,U>,U extends IFace> {

    }

    @QueryEntity
    public abstract class BaseSub extends Base<BaseSub> {

    }

    @QueryEntity
    public abstract class BaseSub2<T extends BaseSub2<T>> extends Base<T> {

    }

    @QueryEntity
    public abstract class Base2Sub<T extends IFace> extends Base2<Base2Sub<T>,T> {

    }

    public interface IFace {

    }
    
    @Test
    public void Base_base() throws SecurityException, NoSuchFieldException {        
        TypeFactory typeFactory = new TypeFactory();
        Field field = Base.class.getDeclaredField("base");
        Type type = typeFactory.get(field.getType(), field.getGenericType());
        assertEquals(0, type.getParameters().size());
    }
    
    @Test
    public void Base_base2() throws SecurityException, NoSuchFieldException {        
        TypeFactory typeFactory = new TypeFactory();
        Field field = Base.class.getDeclaredField("base2");
        Type type = typeFactory.get(field.getType(), field.getGenericType());
        assertEquals(2, type.getParameters().size());
        assertNull(((TypeExtends)type.getParameters().get(0)).getVarName());
        assertNull(((TypeExtends)type.getParameters().get(1)).getVarName());
    }
    
    @Test
    public void test() {
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new File("target/" + getClass().getSimpleName()));
        exporter.export(getClass().getClasses());
    }

}