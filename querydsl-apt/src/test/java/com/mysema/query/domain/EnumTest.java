package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;

public class EnumTest {
    
    @QueryEntity
    public enum Gender {
        MALE,
        FEMALE
    }
    
    @QueryEmbeddable
    public enum Gender2 {
        MALE,
        FEMALE
    }
    
    @QueryEntity
    public static class Bean {
        Gender gender;
    }
    
    @Test
    public void Enum_as_Comparable(){
        assertNotNull(QEnumTest_Gender.gender.asc());        
    }
    
    @Test
    public void EnumOrdinal_as_Comparable(){
        assertNotNull(QEnumTest_Gender.gender.ordinal().asc());        
    }

}
