/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.querydsl.scala.sql

import scala.collection.JavaConversions._

import com.querydsl.sql._
import com.querydsl._
import com.querydsl.core.types.{ Expression, OrderSpecifier, Predicate }

import com.querydsl.scala._
import com.querydsl.scala.RichProjectable
import com.querydsl.scala.Projections._

/**
 * RichSimpleQuery provides a simplified querydsl DSL for Querydsl SQL and Scala
 * 
 * @author tiwe
 *
 * @param <PT>
 * @param <P>
 * @param <T>
 * @param <E>
 */
class RichSimpleQuery[PT, P <: RelationalPath[PT], T, E <: Ex[T]](path: P, expr: E, qry: SQLQuery) 
extends RichProjectable(qry) {
  
  type FEx[X] = P => Ex[X]
  
  def query = this
      
  // TODO : simplify these
  
  def join[T2, R2 <: RelationalPath[T2], T3, E3 <: Ex[T3]](f : E => ForeignKey[T2], rp: R2) 
      (implicit e2t: ExprToTarget[T,E,T2,R2,T3,E3]): RichSimpleQuery[PT,P,T3,E3] = join(f(expr), rp)
  
  def join[T2, R2 <: RelationalPath[T2], T3, E3 <: Ex[T3]](fk: ForeignKey[T2], rp: R2) 
      (implicit e2t: ExprToTarget[T,E,T2,R2,T3,E3]): RichSimpleQuery[PT,P,T3,E3] = {    
    new RichSimpleQuery[PT,P,T3,E3](path, e2t.toTarget(expr, rp), qry.innerJoin(fk, rp))
  }    
  
  def limit(l: Long) = { qry.limit(l); this } 
  
  def offset(l: Long) = { qry.offset(l); this }
  
  def orderBy(f: P => OrderSpecifier[_]) = { qry.orderBy(f(path)); this }  
  
  def orderBy(o: OrderSpecifier[_]*) = { qry.orderBy(o:_*); this }
  
  def where(f: P => Predicate) = { qry.where(f(path)); this }
  
  def where(predicates: Predicate*) = { qry.where(predicates:_*); this }
  
  // projection
  
  def all: List[T] = select

  def count = qry.count()
  
  def select: List[T] = select(expr)
  
  def select[T](f: FEx[T]): List[T] = select(f(path))
  
  def select[T,U](f1: FEx[T], f2: FEx[U]): List[(T,U)] =  select(f1(path), f2(path))
  
  def select[T,U,V](f1: FEx[T], f2: FEx[U], f3: FEx[V]): List[(T,U,V)] = {
    select(f1(path), f2(path), f3(path))
  }
  
  def select[T,U,V,W](f1: FEx[T], f2: FEx[U], f3: FEx[V], f4: FEx[W])
  : List[(T,U,V,W)] = {
    select(f1(path), f2(path), f3(path), f4(path))
  }
  
  def select[T,U,V,W,X](f1: FEx[T], f2: FEx[U], f3: FEx[V], f4: FEx[W], f5: FEx[X])
  : List[(T,U,V,W,X)] = {
    select(f1(path), f2(path), f3(path), f4(path), f5(path))
  }    
  
  def selectGrouped[K,T,V](fkey: FEx[K], fparent: FEx[T], fchild: FEx[V]): List[(T,Set[V])] = {
    selectGrouped(fkey(path), fparent(path), fchild(path))
  }  
  
  def single: Option[T] = single(expr)
  
  def single[T](f: FEx[T]): Option[T] = single(f(path))
  
  def unique: Option[T] = unique(expr)  
  
  def unique[T](f: FEx[T]): Option[T] = unique(f(path))
  
}

object ExprToTarget {
  
  type RP[T] = RelationalPath[T]
  
  // TODO : use closures instead of ExprToTarget
  
  implicit def pathToTuple2[T,E <: RP[T],T2,E2 <: RP[T2]] = {
    new ExprToTarget[T,E,T2,E2,(T,T2),Tu2Ex[T,T2]]() {
    def toTarget(e: E, rp: E2) = new Tu2Ex[T,T2](e, rp)
  }}
  
  implicit def tuple2ToTuple3[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: RP[T3]] = {
    new ExprToTarget[(T1,T2),Tu2Ex[T1,T2],T3,E3,(T1,T2,T3),Tu3Ex[T1,T2,T3]]() {
    def toTarget(e: Tu2Ex[T1,T2], rp: E3) = new Tu3Ex[T1,T2,T3](e(0), e(1), rp)
  }}
  
  implicit def tuple3ToTuple4[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: Ex[T3],T4,E4 <: RP[T4]] = {
    new ExprToTarget[(T1,T2,T3),Tu3Ex[T1,T2,T3],T4,E4,(T1,T2,T3,T4),Tu4Ex[T1,T2,T3,T4]]() {
    def toTarget(e: Tu3Ex[T1,T2,T3], rp: E4) = new Tu4Ex[T1,T2,T3,T4](e(0), e(1), e(2), rp)
  }}
  
  implicit def tuple4ToTuple5[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: Ex[T3],T4,E4 <: Ex[T4],T5,E5 <: RP[T5]] = {
    new ExprToTarget[(T1,T2,T3,T4),Tu4Ex[T1,T2,T3,T4],T5,E5,(T1,T2,T3,T4,T5),Tu5Ex[T1,T2,T3,T4,T5]]() {
    def toTarget(e: Tu4Ex[T1,T2,T3,T4], rp: E5) = new Tu5Ex[T1,T2,T3,T4,T5](e(0), e(1), e(2), e(3), rp)
  }}
  
}

// combine E1 and E2 into E3
trait ExprToTarget[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: Ex[T3]] {
  
  def toTarget(expr: E1, rp: E2): E3 
  
}


