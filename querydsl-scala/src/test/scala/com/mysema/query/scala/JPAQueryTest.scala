package com.mysema.query.scala

import com.mysema.query.scala.Conversions._
import com.mysema.query.jpa.JPQLSubQuery;

import com.mysema.query.types.path._
import org.junit.Test
import org.junit.Assert._

class JPAQueryTest {
     
    var domainType = alias(classOf[DomainType])
    
    @Test
    def OneLiner(){
        query from (domainType) where (domainType.firstName $like "Rob%") unique (domainType);
    }
    
    @Test
    def Projections(){
        query from (domainType) list (domainType)
        query from (domainType) list (domainType.firstName)
        query from (domainType) list (domainType.firstName, domainType.lastName)
        
        query from domainType list domainType.firstName
    }
    
    @Test
    def Filters(){
        query from (domainType) where (domainType.firstName $isEmpty) count;
        query from (domainType) where (domainType.firstName $isEmpty, domainType.lastName $isNotNull) list (domainType);
    }
    
    @Test
    def Order(){
        query from (domainType) orderBy (domainType.firstName asc) list (domainType);        
    }
    
    @Test
    def Various(){
        // list
        query from (domainType) where (domainType.firstName $like "Rob%") list (domainType);         
        // unique result
        query from (domainType) where (domainType.firstName $like "Rob%") unique (domainType);        
        // long where
        query from (domainType) where (domainType.firstName $like "Rob%", domainType.lastName $like "An%") list (domainType)       
    }
        
    def query() = new JPQLSubQuery()
    
}