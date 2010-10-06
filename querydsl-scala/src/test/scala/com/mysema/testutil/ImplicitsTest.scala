package com.mysema.testutil

import org.junit.{ Test, Before, After };
import org.junit.Assert._


class ImplicitsTest {

    implicit def toStringWrapper(str: String) = new StringWrapper(str);

    implicit def toIntWrapper(num: Integer) = new IntegerWrapper(num);
    
    @Test
    def stringValue(){
        val res: Predicate = "str" startsWith "other";
    }
    
    @Test
    def intValue(){
        val num: Integer = 1;
        val res: Predicate = num < 3; 
    }
    
}

class StringWrapper(str: String){
    
    def startsWith(other: String): Predicate = null;
    
}

class IntegerWrapper(num: Integer){
    
    def <(other: Integer): Predicate = null;
}

trait Predicate