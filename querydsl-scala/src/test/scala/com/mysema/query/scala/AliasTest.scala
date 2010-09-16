package com.mysema.query.scala

import com.mysema.query.types.path._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.scala.Conversions._

import org.junit.Test
import org.junit.Assert._


class AliasTest {

    var domainType = alias(classOf[DomainType])
    
//    @Test
//    def Explicit_Cast(){        
//        assertEquals("domainType.firstName", $(domainType.firstName).toString);
//    }
    
    @Test
    def Implicit_Cast1(){
        var path: StringPath = domainType.firstName;
        assertEquals("domainType.firstName like Hello", (path _like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (path _asc).toString());       
        assertEquals("domainType.firstName = Hello",    (path _eq "Hello").toString());
        assertEquals("domainType.firstName != Hello",   (path _ne "Hello").toString());      
    }
    
    @Test
    def Implicit_Cast2(){
        assertEquals("domainType.firstName like Hello", (domainType.firstName _like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (domainType.firstName _asc).toString());
        
        // and
        var andClause = (domainType.firstName _like "An%") _and (domainType.firstName _like "Be%");
        assertEquals("domainType.firstName like An% && domainType.firstName like Be%", andClause.toString);
              
        // or
        var orClause = (domainType.firstName _like "An%") _or (domainType.firstName _like "Be%");
        assertEquals("domainType.firstName like An% || domainType.firstName like Be%", orClause.toString);

        // not
        var notClause = (domainType.firstName _like "An%") _not;
        assertEquals("!domainType.firstName like An%", notClause.toString);
        
        notClause = not (domainType.firstName _like "An%");
        assertEquals("!domainType.firstName like An%", notClause.toString);
        
        // FIXME : "eq" and "ne" are already reserved
//        assertEquals("domainType.firstName = Hello",    (domainType.firstName eq "Hello").toString());
//        assertEquals("domainType.firstName != Hello",   (domainType.firstName ne "Hello").toString());
    }
    
//    @Test
//    def Expression_in_SubQuery(){
//        // list
//        query().from (domainType) 
//          .where (domainType.firstName _like "Rob%")                
//          .orderBy (domainType.firstName _asc)
//          .list (domainType);            // FIXME
//        
//        // unique result
//        query().from (domainType) 
//          .where (domainType.firstName _like "Rob%")                
//          .orderBy (domainType.firstName _asc)
//          .unique (domainType);            // FIXME
//        
//        // long where
//        query().from (domainType)            
//          .where (
//              domainType.firstName _like "Rob%",
//              domainType.lastName _like "An%"
//           )                
//          .orderBy (domainType.firstName _asc)
//          .list (domainType);            // FIXME
//    }
    
    def query() = new SQLSubQuery();
    
}

class DomainType {    
    
    var firstName: String = null;
    
    var lastName: String = null;
    
}