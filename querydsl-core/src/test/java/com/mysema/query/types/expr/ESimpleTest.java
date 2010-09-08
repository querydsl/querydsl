package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.types.Path;
import com.mysema.query.types.path.PString;

public class ESimpleTest {
    
    @Test
    public void as_usage(){
        ESimple<String> str = new PString("str");
        assertEquals("str as alias", str.as("alias").toString());
        assertEquals("str as alias", str.as(new PString("alias")).toString());
    }
    
    @Test
    public void Subclasses_Override_As() throws SecurityException, NoSuchMethodException{
        List<Class<?>> classes = Arrays.<Class<?>>asList(
                EBoolean.class, 
                EComparable.class, 
                EDate.class, 
                EDateTime.class, 
                EEnum.class,
                ENumber.class, 
                ESimple.class,
                EString.class, 
                ETime.class);
        
        for (Class<?> cl : classes){
            Method asPath = cl.getDeclaredMethod("as", Path.class);
            assertEquals(cl, asPath.getReturnType());
            
            Method asString = cl.getDeclaredMethod("as", String.class);
            assertEquals(cl, asString.getReturnType());
        }
    }

}
