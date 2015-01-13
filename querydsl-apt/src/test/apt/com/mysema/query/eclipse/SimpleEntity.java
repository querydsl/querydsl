package com.querydsl.eclipse;

import java.math.BigDecimal;

import com.querydsl.core.annotations.QueryEntity;

@QueryEntity
public class SimpleEntity {
    
    CustomComparable comparableProp;
    
    CustomNumber customNumber;
    
    int intProp;
    
    Integer integerProp;
    
    BigDecimal bigDecimalProp;
    
    String stringProp;
    
    SimpleEntity entityProp;

}

class CustomComparable implements Comparable<CustomComparable> {

    @Override
    public int compareTo(CustomComparable o) {
        return 0;
    }
    
}

class CustomNumber extends Number implements Comparable<CustomNumber> {

    private static final long serialVersionUID = 8683978836725543780L;

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public int compareTo(CustomNumber o) {
        return 0;
    }
    
    
}