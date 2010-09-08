package com.mysema.query.scala

import com.mysema.query.types.path._

import org.junit.Test
import org.junit.Assert._

class ExpressionTest {
    
   var str = new PString("str");
   var num = new PNumber(classOf[Integer],"num");
       
   @Test
   def Operations(){
       // string
       assertEquals("str = a",  (str eq "a").toString);
       assertEquals("str != a", (str ne "a").toString);
       assertEquals("str > a",  (str gt "a").toString);
       assertEquals("str < a",  (str lt "a").toString);
       
       // boolean
       assertEquals("str = a || str = b", ((str eq "a") or (str eq "b")).toString);
       assertEquals("!str = a", (str eq "a").not().toString);
       
       // numeric
//       assertEquals("num + 1", num add 1); // FIXME
   }
   
}


