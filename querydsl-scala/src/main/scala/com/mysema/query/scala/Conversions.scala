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
    
    def not(b: BooleanExpression): BooleanExpression = b.not()
    
    implicit def booleanPath(b: Boolean): BooleanPath = $(b);
    
    implicit def stringPath(s: String): StringPath = $(s);
    
    implicit def datePath(d: java.sql.Date): DatePath[java.sql.Date] = $(d);
    
    implicit def dateTimePath(d: java.util.Date): DateTimePath[java.util.Date] = $(d);
    
    implicit def timePath(t: java.sql.Time): TimePath[java.sql.Time] = $(t);
        
    implicit def comparablePath(c: Comparable[_]): ComparablePath[_] = $(c);
    
    implicit def simplePath(s: Object): SimplePath[_] = $(s);
    
    //implicit def num[N <: Number & Comparable[N]](n: N): PNumber[N] = $(n);
    
}
