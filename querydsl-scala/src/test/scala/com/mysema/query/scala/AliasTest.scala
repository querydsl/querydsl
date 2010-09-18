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
        assertEquals("domainType.firstName = Hello", (domainType.firstName $eq "Hello").toString());
        assertEquals("domainType.firstName != Hello", (domainType.firstName $ne "Hello").toString());
        assertEquals("domainType.firstName != Hello", (domainType.firstName $ne "Hello").toString());
        
        assertEquals("domainType.firstName like Hello", (domainType.firstName $like "Hello").toString());
        assertEquals("domainType.firstName ASC", (domainType.firstName asc).toString());
        
        // append
        assertEquals("domainType.firstName + x", (domainType.firstName $append "x").toString());
        assertEquals("domainType.firstName + x", (domainType.firstName $append "x").toString());
        
        // and
        var andClause = (domainType.firstName $like "An%") $and (domainType.firstName $like "Be%");
        assertEquals("domainType.firstName like An% && domainType.firstName like Be%", andClause.toString);
              
        // or
        var orClause = (domainType.firstName $like "An%") $or (domainType.firstName $like "Be%");
        assertEquals("domainType.firstName like An% || domainType.firstName like Be%", orClause.toString);

        // not
        var notClause = (domainType.firstName $like "An%") not;
        assertEquals("!domainType.firstName like An%", notClause.toString);
        
        notClause = not (domainType.firstName $like "An%");
        assertEquals("!domainType.firstName like An%", notClause.toString);               
        
        // trim
        assertEquals("trim(domainType.firstName)", (domainType.firstName $trim).toString);
        
        // isEmpty
        assertEquals("empty(domainType.firstName)", (domainType.firstName $isEmpty).toString);
    }
    
    @Test
    def Number_Usage(){
        // lt
        assertEquals("domainType.scalaInt < 5", (domainType.scalaInt $lt 5).toString);
        assertEquals("domainType.javaInt < 5", (domainType.javaInt $lt 5).toString);
        
        // between
        assertEquals("domainType.scalaInt between 2 and 3", (domainType.scalaInt $between (2,3)).toString);
        assertEquals("domainType.javaInt between 2 and 3", (domainType.javaInt $between (2,3)).toString);
        
        // arithmetic
        assertEquals("domainType.scalaInt + 3", (domainType.scalaInt $add 3).toString);
        assertEquals("domainType.scalaInt - 3", (domainType.scalaInt $subtract 3).toString);
        assertEquals("domainType.scalaInt / 3", (domainType.scalaInt $divide 3).toString);
        assertEquals("domainType.scalaInt * 3", (domainType.scalaInt $multiply 3).toString);
        assertEquals("domainType.scalaInt * -1", (domainType.scalaInt $negate).toString);
        assertEquals("domainType.scalaInt % 4", (domainType.scalaInt $mod 4).toString);
        assertEquals("round(domainType.scalaInt)", (domainType.scalaInt $round).toString);
        assertEquals("floor(domainType.scalaInt)", (domainType.scalaInt $floor).toString);
        assertEquals("ceil(domainType.scalaInt)", (domainType.scalaInt $ceil).toString);
        
        // casts
        assertEquals("cast(domainType.javaInt,class java.lang.Long)", (domainType.javaInt $longValue).toString);
        assertEquals("cast(domainType.scalaInt,class java.lang.Long)", (domainType.scalaInt $longValue).toString);
        
        // sqrt
        assertEquals("sqrt(domainType.scalaInt)", (domainType.scalaInt $sqrt).toString);
    }
    
    @Test
    def Java_Collection_Usage(){
        // size
        assertEquals("size(domainType.javaCollection)", (domainType.javaCollection $size).toString);
        assertEquals("size(domainType.javaSet)", (domainType.javaSet $size).toString);
        assertEquals("size(domainType.javaList)", (domainType.javaList $size).toString);
        assertEquals("size(domainType.javaMap)", (domainType.javaMap $size).toString);
        
        // is Empty
        assertEquals("empty(domainType.javaCollection)", (domainType.javaCollection $isEmpty).toString);
        assertEquals("empty(domainType.javaSet)", (domainType.javaSet $isEmpty).toString);
        assertEquals("empty(domainType.javaList)", (domainType.javaList $isEmpty).toString);
        assertEquals("empty(domainType.javaMap)", (domainType.javaMap $isEmpty).toString);
        
        // get
        assertEquals("domainType.javaList.get(0) is not null", (domainType.javaList.get(0) $isNotNull).toString);
        assertEquals("domainType.javaMap.get(xxx) is null", (domainType.javaMap.get("xxx") $isNull).toString);
        
        // get + like
        assertEquals("startsWith(domainType.javaMap.get(xxx),X)", (domainType.javaMap.get("xxx") $startsWith "X").toString);
    }
    
    @Test
    def Scala_Collection_Usage(){
        // size
        assertEquals("size(domainType.scalaList)", (domainType.scalaList $size).toString);
        assertEquals("size(domainType.scalaMap)", (domainType.scalaMap $size).toString);
        
        // is Empty
        assertEquals("empty(domainType.scalaList)", (domainType.scalaList $isEmpty).toString);
        assertEquals("empty(domainType.scalaMap)", (domainType.scalaMap $isEmpty).toString);
        
        // get
        assertEquals("domainType.scalaList.get(0) is not null", (domainType.scalaList(0) $isNotNull).toString);
        assertEquals("domainType.scalaList.get(0) is not null", (domainType.scalaList(0) $isNotNull).toString);
        assertEquals("domainType.scalaMap.get(xxx) is null", (domainType.scalaMap("xxx") $isNull).toString);
        
        // contains
        assertEquals("X in domainType.scalaList", (domainType.scalaList $contains "X").toString);
        assertEquals("X in domainType.javaList", (domainType.javaList $contains "X").toString);
        
        // get + like
        assertEquals("startsWith(domainType.scalaMap.get(xxx),X)", (domainType.scalaMap("xxx") $startsWith "X").toString);
    }
    
    @Test
    def Array_Usage(){
        assertEquals("size(domainType.array)", (domainType.array $size).toString());
    }
        
}

