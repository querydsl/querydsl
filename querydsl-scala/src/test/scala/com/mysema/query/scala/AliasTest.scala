package com.mysema.query.scala;

import com.mysema.query.scala.Conversions._
import com.mysema.query.sql.SQLSubQuery

import com.mysema.query.types.path._
import org.junit.Test
import org.junit.Assert._


class AliasTest {

    var domainType = alias(classOf[DomainType])

    @Test
    def String_Usage(){
        // eq, ne
        assertEquals("domainType.firstName = Hello", (domainType.firstName _eq "Hello").toString());
        assertEquals("domainType.firstName != Hello", (domainType.firstName _ne "Hello").toString());
        
        assertEquals("domainType.firstName like Hello", (domainType.firstName _like "Hello").toString());
        assertEquals("domainType.firstName ASC", (domainType.firstName _asc).toString());
        
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
    }
    
    @Test
    def Java_Collection_Usage(){
        // size
        assertEquals("size(domainType.javaCollection)", (domainType.javaCollection _size).toString);
        assertEquals("size(domainType.javaSet)", (domainType.javaSet _size).toString);
        assertEquals("size(domainType.javaList)", (domainType.javaList _size).toString);
        assertEquals("size(domainType.javaMap)", (domainType.javaMap _size).toString);
        
        // is Empty
        assertEquals("empty(domainType.javaCollection)", (domainType.javaCollection _isEmpty).toString);
        assertEquals("empty(domainType.javaSet)", (domainType.javaSet _isEmpty).toString);
        assertEquals("empty(domainType.javaList)", (domainType.javaList _isEmpty).toString);
        assertEquals("empty(domainType.javaMap)", (domainType.javaMap _isEmpty).toString);
        
        // get
        assertEquals("domainType.javaList.get(0) is not null", (domainType.javaList.get(0) _isNotNull).toString);
        assertEquals("domainType.javaMap.get(xxx) is null", (domainType.javaMap.get("xxx") _isNull).toString);
        
        // get + like
        assertEquals("startsWith(domainType.javaMap.get(xxx),X)", (domainType.javaMap.get("xxx") _startsWith "X").toString);
    }
    
    @Test
    def Scala_Collection_Usage(){
        // size
        assertEquals("size(domainType.scalaList)", (domainType.scalaList _size).toString);
        assertEquals("size(domainType.scalaMap)", (domainType.scalaMap _size).toString);
        
        // is Empty
        assertEquals("empty(domainType.scalaList)", (domainType.scalaList _isEmpty).toString);
        assertEquals("empty(domainType.scalaMap)", (domainType.scalaMap _isEmpty).toString);
        
        // get
        assertEquals("domainType.scalaList.get(0) is not null", (domainType.scalaList(0) _isNotNull).toString);
        assertEquals("domainType.scalaList.get(0) is not null", (domainType.scalaList(0) _isNotNull).toString);
        assertEquals("domainType.scalaMap.get(xxx) is null", (domainType.scalaMap("xxx") _isNull).toString);
        
        // get + like
        assertEquals("startsWith(domainType.scalaMap.get(xxx),X)", (domainType.scalaMap("xxx") _startsWith "X").toString);
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

