package com.mysema.query.collections.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.mysema.query.annotations.Entity;

@Entity
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
}