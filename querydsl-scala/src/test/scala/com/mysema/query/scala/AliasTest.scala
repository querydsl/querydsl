package com.mysema.query.scala

import com.mysema.query.alias.Alias._
import com.mysema.query.types.path._

import com.mysema.query.scala.Conversions._

import org.junit.Test
import org.junit.Assert._


class AliasTest {

    var domainType = alias(classOf[DomainType])
    
    @Test
    def test(){        
        assertEquals("domainType.firstName", $(domainType.firstName).toString);
    }
    
    @Test
    def implicitDefs1(){
        var path: PString = domainType.firstName;
        assertEquals("domainType.firstName like Hello", (path like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (path asc).toString());       
        assertEquals("domainType.firstName = Hello",    (path eq "Hello").toString());
        assertEquals("domainType.firstName != Hello",   (path ne "Hello").toString());      
    }
    
    @Test
    def implicitDefs2(){
        assertEquals("domainType.firstName like Hello", (domainType.firstName like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (domainType.firstName asc).toString());
        
        // FIXME : "eq" and "ne" are already reserved
//        assertEquals("domainType.firstName = Hello",    (domainType.firstName eq "Hello").toString());
//        assertEquals("domainType.firstName != Hello",   (domainType.firstName ne "Hello").toString());
    }
    
}

class DomainType {    
    var firstName: String = null;
    var lastName: String = null;
}