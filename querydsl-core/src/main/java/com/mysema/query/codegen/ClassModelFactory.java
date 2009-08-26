package com.mysema.query.codegen;

import java.lang.reflect.Field;

import net.jcip.annotations.Immutable;

/**
 * @author tiwe
 *
 */
@Immutable
public class ClassModelFactory {
    
    private final TypeModelFactory typeModelFactory;
    
    public ClassModelFactory(TypeModelFactory typeModelFactory){
        this.typeModelFactory = typeModelFactory;
    }
    
    public ClassModel create(Class<?> key, String prefix ){
        ClassModel classModel = new ClassModel(
                this,
                prefix,
                key.getSuperclass().getName(), 
                key.getPackage().getName(), 
                key.getName(), 
                key.getSimpleName());
        for (Field f : key.getDeclaredFields()) {
            TypeModel typeModel = typeModelFactory.create(f.getType(), f.getGenericType());
            classModel.addField(new FieldModel(classModel, f.getName(), typeModel, f.getName()));
        }
        return classModel;
    }
    
}
