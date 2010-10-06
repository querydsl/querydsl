package com.mysema.testutil

import org.junit.{ Test, Before, After };
import org.junit.Assert._


class ImplicitsTest {

    implicit def toStringWrapper(str: String) = new StringWrapper(str);

    implicit def toIntegerWrapper(num: Integer) = new IntegerWrapper(num);
    
    implicit def toIntWrapper(num: Int) = new IntWrapper(num);
    
    @Test
    def stringValue(){
        val res: Predicate = "str" startsWith "other";
    }
    
    @Test
    def integerValue(){
        val num: Integer = 1;
        val res1: Predicate = num < 3; 
        val res2: Predicate = num lt 3;
    }
    
    @Test
    def intValue(){
        val num: Int = 1;
        val res1: Predicate = num < 3;
        val res2: Predicate = num lt 3;
    }
    
}

class StringWrapper(str: String){
    
    def startsWith(other: String): Predicate = null;
    
}

class IntegerWrapper(num: Integer){
    
    def <(other: Integer): Predicate = null;
    
    def lt(other: Integer): Predicate = null;
}

class IntWrapper(num: Int){
    
    def <(other: Integer): Predicate = null;
    
    def lt(other: Integer): Predicate = null;
}

trait Predicate