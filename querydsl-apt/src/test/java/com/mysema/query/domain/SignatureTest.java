package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Test;

import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.path.ComparablePath;

public class SignatureTest {
    
    @QuerySupertype
    public abstract class APropertyChangeSupported implements Comparable<Object>, Cloneable, Serializable {
        
    }
   
   
    @QuerySupertype
    public abstract class AValueObject extends APropertyChangeSupported implements Comparable<Object>, Cloneable, Serializable {
        
    }
    
    @Test
    public void APropertyChangeSupported() {
        assertEquals(ComparablePath.class, QSignatureTest_APropertyChangeSupported.class.getSuperclass());
    }
    
    @Test
    public void AValueObject() {
        assertEquals(ComparablePath.class, QSignatureTest_AValueObject.class.getSuperclass());
    }

}
