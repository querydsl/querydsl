package com.mysema.query.codegen;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class ClassTypeModel implements TypeModel{
    
    private final TypeCategory typeCategory;
    
    private final Class<?> clazz;
    
    private final Class<?> primitiveClass;
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz){
        this.typeCategory = Assert.notNull(typeCategory);
        this.clazz = Assert.notNull(clazz);
        this.primitiveClass = ClassUtils.wrapperToPrimitive(clazz);
    }

    @Override
    public TypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new ClassTypeModel(category, clazz);
        }
    }

    @Override
    public TypeModel getKeyType() {
        return null;
    }

    @Override
    public String getLocalName() {
        return clazz.getName().substring(clazz.getPackage().getName().length()+1);
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public String getPackageName() {
        return clazz.getPackage().getName();
    }

    @Override
    public String getPrimitiveName() {
        return primitiveClass != null ? primitiveClass.getSimpleName() : null;
    }

    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    @Override
    public TypeModel getValueType() {
        return null;
    }

    @Override
    public boolean isPrimitive() {
        return primitiveClass != null;
    }

}
