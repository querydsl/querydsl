package com.mysema.query.scala

import org.junit.Test
import org.junit.Assert._

class ExpressionTest {
    
   var str = new StringPath("str");
   var num = new NumberPath[Integer](classOf[Integer],"num");
   
   @Test
   def String(){
       assertEquals("count(str)",    str._count().toString);
       assertEquals("str in [a, b]", str._in("a","b").toString);
       //assertEquals("", str._in(List("a","b")).toString);
   }
       
   @Test
   def Operations(){
       // string
       assertEquals("str = a",  (str _eq "a").toString);
       assertEquals("str != a", (str _ne "a").toString);
       assertEquals("str > a",  (str _gt "a").toString);
       assertEquals("str < a",  (str _lt "a").toString);
       
       // boolean
       assertEquals("str = a || str = b", ((str _eq "a") _or (str _eq "b")).toString);
       assertEquals("!str = a", (str _eq "a")._not().toString);
       
       // numeric
//       assertEquals("num + 1", num add 1); // FIXME
   }
   
   
   
   
}


