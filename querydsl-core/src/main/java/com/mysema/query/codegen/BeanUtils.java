package com.mysema.query.codegen;

import java.beans.Introspector;

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
public final class BeanUtils {

    public static String capitalize(String property){
        if (property.length() > 1 && Character.isUpperCase(property.charAt(1))){
            return property;
        }else{
            return StringUtils.capitalize(property);
        }
    }

    public static String uncapitalize(String name){
        return Introspector.decapitalize(name);
    }

    private BeanUtils(){}
}
