package com.mysema.testutil

import org.junit.{ Test, Before, After };
import org.junit.Assert._


class ImplicitsTest {

    implicit def toStringWrapper(str: String) = new StringWrapper(str);

    @Test
    def test(){
        val res1: Predicate = "str" startsWith "other";
    }
    
}

class StringWrapper(str: String){
    
    def startsWith(other: String): Predicate = null;
    
}

trait Predicate