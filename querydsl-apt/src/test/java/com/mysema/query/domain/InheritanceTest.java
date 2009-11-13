package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class InheritanceTest {
    
    @QueryEntity
    public abstract class Entity{
        
    }
    
    @QueryEntity
    public abstract class Party<A extends PartyRole> extends Entity{
        
    }
    
    @QueryEntity
    public interface PartyRole{
        
    }
    
    @QueryEntity
    public class Person extends Party<PersonRole>{
        
    }
    
    @QueryEntity
    public interface PersonRole extends PartyRole{
        
    }
    
    @Test
    public void test(){
        // TODO
    }

}
