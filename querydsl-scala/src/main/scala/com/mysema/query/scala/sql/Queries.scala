package com.mysema.query.scala.sql

import scala.collection.JavaConversions._

import com.mysema.query.sql._
import com.mysema.query._
import com.mysema.query.types.{ Expression, OrderSpecifier, Predicate }

import com.mysema.query.scala._
import com.mysema.query.scala.TypeDefs._
import com.mysema.query.scala.RichProjectable
import com.mysema.query.scala.Projections._

object RichSimpleQuery {
  
  type RP[T] = RelationalPath[T]
  
  // TODO : use closures instead of ExprToTarget
  
  implicit def pathToTuple2[T,E <: RP[T],T2,E2 <: RP[T2]] = {
    new ExprToTarget[T,E,T2,E2,(T,T2),Tu2Ex[T,T2,E,E2]]() {
    def toTarget(e: E, rp: E2) = new Tu2Ex[T,T2,E,E2](e,rp)
  }}
  
  implicit def tuple2ToTuple3[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: RP[T3]] = {
    new ExprToTarget[(T1,T2),Tu2Ex[T1,T2,E1,E2],T3,E3,(T1,T2,T3),Tu3Ex[T1,T2,T3,E1,E2,E3]]() {
    def toTarget(e: Tu2Ex[T1,T2,E1,E2], rp: E3) = new Tu3Ex[T1,T2,T3,E1,E2,E3](e._1, e._2, rp)
  }}
  
}

class RichSimpleQuery[T, E <: Ex[T]](expr: E, qry: SQLQuery) extends RichProjectable(qry) {
  
  def query = this
  
  def limit(l: Long) = { qry.limit(l); this } 
  
  def offset(l: Long) = { qry.offset(l); this }
  
  def orderBy(f: E => OrderSpecifier[_]) = { qry.orderBy(f(expr)); this }  
  
  def orderBy(o: OrderSpecifier[_]*) = { qry.orderBy(o:_*); this }
  
  def where(f: E => Predicate) = { qry.where(f(expr)); this }
  
  def where(predicates: Predicate*) = { qry.where(predicates:_*); this }

  def count = qry.count()
  
  def all: List[T] = select
  
  def select: List[T] = select(expr)
  
  def select[T](f: E => Ex[T]): List[T] = select(f(expr))
  
  def single: Option[T] = single(expr)
  
  def unique: Option[T] = unique(expr)  
      
  def join[T2, R2 <: RelationalPath[T2], T3, E3 <: Ex[T3]](f : E => ForeignKey[T2], rp: R2) 
      (implicit e2t: ExprToTarget[T,E,T2,R2,T3,E3]): RichSimpleQuery[T3,E3] = join(f(expr), rp)
  
  def join[T2, R2 <: RelationalPath[T2], T3, E3 <: Ex[T3]](fk: ForeignKey[T2], rp: R2) 
      (implicit e2t: ExprToTarget[T,E,T2,R2,T3,E3]): RichSimpleQuery[T3,E3] = {    
    new RichSimpleQuery[T3,E3](e2t.toTarget(expr, rp), qry.innerJoin(fk, rp))
  }  
}

// combine E1 and E2 into E3
trait ExprToTarget[T1,E1 <: Ex[T1],T2,E2 <: Ex[T2],T3,E3 <: Ex[T3]] {
  
  def toTarget(expr: E1, rp: E2): E3 
  
}


