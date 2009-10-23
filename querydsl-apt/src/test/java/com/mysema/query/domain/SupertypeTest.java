package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class SupertypeTest {
    
    @QuerySupertype
    public static class IdEntity{
        
        public Long idField;
        
    }
    
    @QuerySupertype
    public static class DateEnabled extends IdEntity{
     
        public Date dateField;
    }
    
    @QueryEntity
    public static class AEntity1 extends DateEnabled{
        
        public AEntity1 ref;
    }
    
    @QueryEntity
    public static class WEntity2 extends AEntity1{
        
        public String onlySimpleFields;
        
    }
    
    @Test
    public void test(){
        assertNotNull(QWEntity2.wEntity2.idField);
    }

}
