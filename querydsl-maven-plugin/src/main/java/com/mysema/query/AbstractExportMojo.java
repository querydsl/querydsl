/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;

/**
 * AbstractExportMojo provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractExportMojo extends AbstractMojo{

    /** @parameter */
    protected String namePrefix;

    /** @parameter */
    protected String packageName;
    
    /** @parameter */
    protected boolean camelCase;
    
    protected void expandProperties(InputStream props) throws Exception{
        Properties p = new Properties();
        p.load(props);
        Class<?> cl = getClass();
        while (AbstractExportMojo.class.isAssignableFrom(cl)){
            for (Field field : getClass().getDeclaredFields()){
                if (field.getType().equals(String.class)) expandFieldValue(field, p);                
            }    
            cl = cl.getSuperclass();
        }                        
    }

    private void expandFieldValue(Field field, Properties p) throws IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);        
        String val = (String) field.get(this);
        if (val != null && val.startsWith("%{")){
             field.set(this, p.get(val.subSequence(2, val.length()-1)));    
        }
    }
    
}
