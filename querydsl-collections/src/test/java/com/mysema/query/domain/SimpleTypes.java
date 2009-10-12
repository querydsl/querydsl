/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.literals.CustomComparableLiteral;
import com.mysema.query.literals.CustomLiteral;

@QueryEntity
public class SimpleTypes {

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

    @QueryTransient
    String skipMe;
}