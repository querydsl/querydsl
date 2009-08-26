package com.mysema.query.collections.domain;

import static org.junit.Assert.fail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.mysema.query.annotations.Entity;
import com.mysema.query.annotations.Transient;

@Entity
public class SimpleTypes {
    
    @Test
    public void test() throws SecurityException{
        try {
            QSimpleTypes.class.getField("skipMe");
            fail("Expected NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // expected
        }
    }
    
    transient int test;
    
    long id;
    
    BigDecimal bigDecimal;
    
    Byte bbyte;
    
    byte bbyte2;
    
    Character cchar;
    
    char cchar2;
    
    Double ddouble;
    
    double ddouble2;
    
    Float ffloat;
    
    float ffloat2;
    
    Integer iint;
    
    int iint2;
    
    Locale llocale;
    
    Long llong;
    
    long llong2;
    
    String sstring;
    
    Date date;
    
    java.sql.Time time;
    
    java.sql.Timestamp timestamp;
    
    Serializable serializable;
    
    Object object;
    
    Class<?> clazz;
    
    Package packageAsLiteral;
    
    CustomLiteral literal;
    
    CustomComparableLiteral literal2;
    
    java.sql.Clob clob;
    
    java.sql.Blob blob;
    
    @Transient
    String skipMe;
}