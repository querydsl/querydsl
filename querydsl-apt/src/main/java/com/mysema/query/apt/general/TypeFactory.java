package com.mysema.query.apt.general;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.mysema.query.apt.model.Field;
import com.mysema.query.apt.model.Type;

/**
 * TypeFactory provides
 *
 * @author tiwe
 * @version $Id$
 */
public class TypeFactory {

    public static Type createType(Class<?> clazz) {
        Type type = new Type(clazz.getSuperclass().getName(), 
                clazz.getPackage().getName(), 
                clazz.getName(), 
                clazz.getSimpleName());
        for (java.lang.reflect.Field f : clazz.getDeclaredFields()){
            TypeHelper typeHelper = new TypeHelper(f.getType(),f.getGenericType());
            Field field = new Field(
                    FieldHelper.javaSafe(f.getName()), // name 
                    FieldHelper.realName(f.getName()), // realName
                    null,  // keyTypeName
                    typeHelper.getPackageName(),
                    typeHelper.getFullName(),
                    typeHelper.getSimpleName(),
                    typeHelper.getFieldType());
            type.addField(field);
        }
        return type;
    }
    
    // TODO : move this to common place
    public static Class<?> getTypeParameter(java.lang.reflect.Type type, int index) {
        if (type instanceof ParameterizedType){
            ParameterizedType ptype = (ParameterizedType) type;
            java.lang.reflect.Type[] targs = ptype.getActualTypeArguments();
            if (targs[index] instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) targs[index]; 
                return (Class<?>) wildcardType.getUpperBounds()[0];
            } else if (targs[index] instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable) targs[index]).getGenericDeclaration();
            } else if (targs[index] instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) targs[index]).getRawType();
            } else {
                try {
                    return (Class<?>) targs[index];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
