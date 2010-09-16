/*
 * Copyright (c) 2010 Mysema Ltd. 
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.alias._;

import com.mysema.query.types._
import com.mysema.query.types.expr._
import com.mysema.query.types.path._

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
object Conversions {
    
    val aliasFactory = new AliasFactory(new PathFactoryImpl());
    
    def not(b: BooleanExpression) = b._not()
    
    def alias[T](cl: Class[T]): T = alias(cl, StringUtils.uncapitalize(cl.getSimpleName)); 

    def alias[T](cl: Class[T], variable: String): T = aliasFactory.createAliasForVariable(cl, variable);
    
    def alias[T](cl: Class[T], expr: Expression[_ <: T]): T = aliasFactory.createAliasForExpr(cl, expr);
    
    implicit def booleanPath(b: java.lang.Boolean): BooleanPath = aliasFactory.getCurrentAndReset();
    
    implicit def stringPath(s: String): StringPath = aliasFactory.getCurrentAndReset();
    
    implicit def datePath(d: java.sql.Date): DatePath[java.sql.Date] = aliasFactory.getCurrentAndReset();
    
    implicit def dateTimePath(d: java.util.Date): DateTimePath[java.util.Date] = aliasFactory.getCurrentAndReset();
    
    implicit def timePath(t: java.sql.Time): TimePath[java.sql.Time] = aliasFactory.getCurrentAndReset();
        
    implicit def comparablePath(c: Comparable[_]): ComparablePath[_] = aliasFactory.getCurrentAndReset();
    
    implicit def simplePath(s: Object): SimplePath[_] = aliasFactory.getCurrentAndReset();
    
    //implicit def num[N <: Number with Comparable[N]](n: N): NumberPath[N] = $(n);
    
}

class PathFactoryImpl extends PathFactory {
    
    // TODO
    def createArrayPath[T](t: Class[Array[T with Object]], md: PathMetadata[_]) = null;
    
    def createEntityPath[T](t: Class[T], md: PathMetadata[_]) = Paths.entity(t, md);
    
    def createSimplePath[T](t: Class[T], md: PathMetadata[_]) = Paths.simple(t, md);
    
    def createComparablePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.comparable(t, md);
    
    def createEnumPath[T <: Enum[T]](t: Class[T], md: PathMetadata[_]) = Paths.enum(t, md);
    
    def createDatePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.date(t, md);
    
    def createTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.time(t, md);
    
    def createDateTimePath[T <: Comparable[_]](t: Class[T], md: PathMetadata[_]) = Paths.dateTime(t, md);
    
    def createNumberPath[T <: Number with Comparable[T]](t: Class[T], md: PathMetadata[_]) = Paths.number(t, md);
    
    def createBooleanPath(md: PathMetadata[_]) = Paths.boolean(md);
    
    def createStringPath(md: PathMetadata[_]) = Paths.string(md);
    
    def createListPath[T](t: Class[T], md: PathMetadata[_]) = Paths.list(t, md);

    def createSetPath[T](t: Class[T], md: PathMetadata[_]) = Paths.set(t, md);
    
    def createCollectionPath[T](t: Class[T], md: PathMetadata[_]) = Paths.collection(t, md);
    
    def createMapPath[K,V](k: Class[K], v: Class[V], md: PathMetadata[_]) = Paths.map(k, v, md);
    
}
