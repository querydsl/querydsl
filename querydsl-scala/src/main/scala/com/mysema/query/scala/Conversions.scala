/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.alias.Alias._
import com.mysema.query.types.expr._
import com.mysema.query.types.path._

/**
 * @author tiwe
 *
 */
object Conversions {
    
    def not(b: EBoolean): EBoolean = b.not()
    
    implicit def booleanPath(b: Boolean): PBoolean = $(b);
    
    implicit def stringPath(s: String): PString = $(s);
    
    implicit def datePath(d: java.sql.Date): PDate[java.sql.Date] = $(d);
    
    implicit def dateTimePath(d: java.util.Date): PDateTime[java.util.Date] = $(d);
    
    implicit def timePath(t: java.sql.Time): PTime[java.sql.Time] = $(t);
        
    implicit def comparablePath(c: Comparable[_]): PComparable[_] = $(c);
    
    implicit def simplePath(s: Object): PSimple[_] = $(s);
    
    //implicit def num[N <: Number & Comparable[N]](n: N): PNumber[N] = $(n);
    
}
