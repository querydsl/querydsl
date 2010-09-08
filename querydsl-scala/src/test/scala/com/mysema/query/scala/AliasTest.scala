package com.mysema.query.scala

import com.mysema.query.alias.Alias._
import com.mysema.query.types.path._

import org.junit.Test
import org.junit.Assert._

class AliasTest {

    var domainType = alias(classOf[DomainType])
    
    implicit def _boolean(b: Boolean): PBoolean = $(b);
    implicit def _string(s: String): PString = $(s);
    implicit def _comparable(c: Comparable[_]): PComparable[_] = $(c);
    implicit def _date(d: java.sql.Date): PDate[java.sql.Date] = $(d);
    implicit def _dateTime(d: java.util.Date): PDateTime[java.util.Date] = $(d);
    implicit def _time(t: java.sql.Time): PTime[java.sql.Time] = $(t);
    //implicit def num(num: Number): PNumber[_] = $(num);
    
    
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
//        assertEquals("domainType.firstName = Hello",    (domainType.firstName eq "Hello").toString());
//        assertEquals("domainType.firstName != Hello",   (domainType.firstName ne "Hello").toString());
    }
    
}

class DomainType {    
    var firstName: String = null;
    var lastName: String = null;
}