package com.mysema.query.scala

import com.mysema.query.alias.Alias._
import com.mysema.query.types.path._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.scala.Conversions._

import org.junit.Test
import org.junit.Assert._


class AliasTest {

    var domainType = alias(classOf[DomainType])
    
    @Test
    def Explicit_Cast(){        
        assertEquals("domainType.firstName", $(domainType.firstName).toString);
    }
    
    @Test
    def Implicit_Cast1(){
        var path: StringPath = domainType.firstName;
        assertEquals("domainType.firstName like Hello", (path like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (path asc).toString());       
        assertEquals("domainType.firstName = Hello",    (path eq "Hello").toString());
        assertEquals("domainType.firstName != Hello",   (path ne "Hello").toString());      
    }
    
    @Test
    def Implicit_Cast2(){
        assertEquals("domainType.firstName like Hello", (domainType.firstName like "Hello").toString());
        assertEquals("domainType.firstName ASC",        (domainType.firstName asc).toString());
        
        // and
        var andClause = (domainType.firstName like "An%") and (domainType.firstName like "Be%");
        assertEquals("domainType.firstName like An% && domainType.firstName like Be%", andClause.toString);
              
        // or
        var orClause = (domainType.firstName like "An%") or (domainType.firstName like "Be%");
        assertEquals("domainType.firstName like An% || domainType.firstName like Be%", orClause.toString);

        // not
        var notClause = (domainType.firstName like "An%") not;
        assertEquals("!domainType.firstName like An%", notClause.toString);
        
        notClause = not (domainType.firstName like "An%");
        assertEquals("!domainType.firstName like An%", notClause.toString);
        
        // FIXME : "eq" and "ne" are already reserved
//        assertEquals("domainType.firstName = Hello",    (domainType.firstName eq "Hello").toString());
//        assertEquals("domainType.firstName != Hello",   (domainType.firstName ne "Hello").toString());
    }
    
    @Test
    def Expression_in_SubQuery(){
        // list
        query().from ($(domainType)) 
          .where (domainType.firstName like "Rob%")                
          .orderBy (domainType.firstName asc)
          .list ($(domainType));            // FIXME
        
        // unique result
        query().from ($(domainType)) 
          .where (domainType.firstName like "Rob%")                
          .orderBy (domainType.firstName asc)
          .unique ($(domainType));            // FIXME
        
        // long where
        query().from ($(domainType))            
          .where (
              domainType.firstName like "Rob%",
              domainType.lastName like "An%"
           )                
          .orderBy (domainType.firstName asc)
          .list ($(domainType));            // FIXME
    }
    
    def query() = new SQLSubQuery();
    
}

class DomainType {    
    var firstName: String = null;
    var lastName: String = null;
}