package com.mysema.query.scala;

import com.mysema.query.types._
import java.util.{ Arrays }

import TypeDefs._

object Projections extends Projections

trait Projections {

  // TODO : simplify this 
  
  implicit def tuple2Expr[T1,T2,E1 <: Ex[T1],E2 <: Ex[T2]](t: (Ex[T1] with E1,Ex[T2] with E2)) = {
    new Tuple2Expression[T1,T2,E1,E2](t._1, t._2) 
  }

  implicit def tuple3Expr[T1,T2,T3,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3]](t: (Ex[T1] with E1,Ex[T2] with E2,Ex[T3] with E3)) = {
    new Tuple3Expression[T1,T2,T3,E1,E2,E3](t._1, t._2, t._3)
  }

  implicit def tuple4Expr[T1,T2,T3,T4,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4]](t: (Ex[T1] with E1,Ex[T2] with E2,Ex[T3] with E3,Ex[T4] with E4)) = {
    new Tuple4Expression[T1,T2,T3,T4,E1,E2,E3,E4](t._1, t._2, t._3, t._4)
  }
  
  implicit def tuple5Expr[T1,T2,T3,T4,T5,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4],E5 <: Ex[T5]](t: (E1 with Ex[T1],E2 with Ex[T2],E3 with Ex[T3],E4 with Ex[T4],E5 with Ex[T5])) = {
    new Tuple5Expression[T1,T2,T3,T4,T5,E1,E2,E3,E4,E5](t._1, t._2, t._3, t._4, t._5)
  }
     
}

class Tuple2Expression[T1,T2,E1 <: Ex[T1], E2 <: Ex[T2]](val _1: E1, val _2: E2) 
extends FactoryExpressionBase[(T1,T2)](classOf[Tuple2[T1,T2]], _1, _2) {
  
  def newInstance(args: AnyRef*): (T1,T2) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2]) 
  }     
}

class Tuple3Expression[T1,T2,T3,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3]](val _1: E1, val _2: E2, val _3: E3) 
extends FactoryExpressionBase[Tuple3[T1,T2,T3]](classOf[Tuple3[T1,T2,T3]], _1, _2, _3) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3])
  }   
}

class Tuple4Expression[T1,T2,T3,T4,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4]](val _1: E1, val _2: E2, val _3: E3, val _4: E4) 
extends FactoryExpressionBase[Tuple4[T1,T2,T3,T4]](classOf[Tuple4[T1,T2,T3,T4]], _1, _2, _3, _4) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4])
  }   
}

class Tuple5Expression[T1,T2,T3,T4,T5,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4],E5 <: Ex[T5]](val _1: E1, val _2: E2, val _3: E3, val _4: E4, val _5: E5) 
extends FactoryExpressionBase[Tuple5[T1,T2,T3,T4,T5]](classOf[Tuple5[T1,T2,T3,T4,T5]], _1, _2, _3, _4, _5) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4], args(4).asInstanceOf[T5])
  }   
}

abstract class FactoryExpressionBase[T](cl: Class[T], args: Ex[_]*) extends ExpressionBase[T](cl) with FactoryExpression[T] {
  
  def accept[R,C](v: Visitor[R,C], context: C): R = v.visit(this, context)
  
  def getArgs(): JavaList[Ex[_]] = Arrays.asList(args:_*)
  
} 
