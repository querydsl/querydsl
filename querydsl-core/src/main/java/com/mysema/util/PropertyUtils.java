package com.mysema.util;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;


/**
 * @author tiwe
 *
 */
public final class PropertyUtils {
    
    private PropertyUtils(){}

    @Nullable
    public static AnnotatedElement getAnnotatedElement(Class<?> beanClass, String propertyName, Class<?> propertyClass){
        Field field = getField(beanClass, propertyName);
        Method method = getGetter(beanClass, propertyName, propertyClass);
        if (field == null || field.getAnnotations().length == 0){
            return method;
        }else if (method == null || method.getAnnotations().length == 0){
            return field;
        }else{
            return new AnnotatedElementAdapter(field, method);
        }
    }
    
    @Nullable
    private static Field getField(Class<?> beanClass, String propertyName){
        while (!beanClass.equals(Object.class)){
            try {
                return beanClass.getDeclaredField(propertyName);
            } catch (SecurityException e) { // skip
            } catch (NoSuchFieldException e) { // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }
    
    @Nullable
    private static Method getGetter(Class<?> beanClass, String name, Class<?> type){
        String methodName = (type.equals(Boolean.class) ? "is" : "get") + StringUtils.capitalize(name);
        while(!beanClass.equals(Object.class)){
            try {
                return beanClass.getDeclaredMethod(methodName);                
            } catch (SecurityException e) { // skip
            } catch (NoSuchMethodException e) { // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
        
    }
    
}
