/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SimpleTypes {
    transient int test;
    @Id
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
}