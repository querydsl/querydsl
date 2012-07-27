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

package com.mysema.query.scala

import com.mysema.query.types._
import com.mysema.query.types.{ Projections => ProjectionsFactory }
import java.util.{ Arrays }

object Projections extends Projections

/**
 * Projections provides implicit conversions from tuples to FactoryExpressions
 * 
 * @author tiwe
 *
 */
trait Projections {
  
  /**
   * Create a FactoryExpressions for the given type and arguments
   *  
   * @param expressions
   * @param mf
   * @return
   */
  def create[T](expressions: Ex[_]*)(implicit mf: Manifest[T]) = {
    val clazz = mf.erasure.asInstanceOf[Class[T]]
    try {
      clazz.getConstructor()
      ProjectionsFactory.fields[T](clazz, expressions:_*)
    } catch { case e: NoSuchMethodException => {
      ProjectionsFactory.constructor[T](clazz, expressions:_*)   
    }}    
  }
  
  implicit def tuple2Expr[T1,T2](t: (_ <: Ex[T1], _ <: Ex[T2])) = new Tu2Ex[T1,T2](t._1, t._2) 

  implicit def tuple3Expr[T1,T2,T3](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3])) = {
    new Tu3Ex[T1,T2,T3](t._1, t._2, t._3) 
  }

  implicit def tuple4Expr[T1,T2,T3,T4](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], _ <: Ex[T4])) = {
    new Tu4Ex[T1,T2,T3,T4](t._1, t._2, t._3, t._4)
  }
  
  implicit def tuple5Expr[T1,T2,T3,T4,T5](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], _ <: Ex[T4], 
      _<: Ex[T5])) = {
    new Tu5Ex[T1,T2,T3,T4,T5](t._1, t._2, t._3, t._4, t._5)
  }
  
  implicit def tuple6Expr[T1,T2,T3,T4,T5,T6](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], _ <: Ex[T4], 
      _<: Ex[T5], _ <: Ex[T6])) = {
    new Tu6Ex[T1,T2,T3,T4,T5,T6](t._1, t._2, t._3, t._4, t._5, t._6)
  }
  
  implicit def tuple7Expr[T1,T2,T3,T4,T5,T6,T7](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], _ <: Ex[T4], 
      _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7])) = {
    new Tu7Ex[T1,T2,T3,T4,T5,T6,T7](t._1, t._2, t._3, t._4, t._5, t._6, t._7)
  }
  
  implicit def tuple8Expr[T1,T2,T3,T4,T5,T6,T7,T8](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], _ <: Ex[T4], 
      _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8])) = {
    new Tu8Ex[T1,T2,T3,T4,T5,T6,T7,T8](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
  }
  
  implicit def tuple9Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3],
      _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9])) = {
    new Tu9Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
  }
  
  implicit def tuple10Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10](t: (_ <: Ex[T1], _ <: Ex[T2], _ <: Ex[T3], 
      _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], _ <: Ex[T10])) = {
    new Tu10Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
  }
  
  implicit def tuple11Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11](t: (_ <: Ex[T1], _ <: Ex[T2], 
      _ <: Ex[T3], _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], 
      _ <: Ex[T10], _ <: Ex[T11])) = {
    new Tu11Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, 
        t._9, t._10, t._11)
  }
  
  implicit def tuple12Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12](t: (_ <: Ex[T1], _ <: Ex[T2], 
      _ <: Ex[T3], _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], 
      _ <: Ex[T10], _ <: Ex[T11], _ <: Ex[T12])) = {
    new Tu12Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12)
  }
  
  implicit def tuple13Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13](t: (_ <: Ex[T1], _ <: Ex[T2], 
      _ <: Ex[T3], _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], 
      _ <: Ex[T10], _ <: Ex[T11], _ <: Ex[T12], _ <: Ex[T13])) = {
    new Tu13Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13)
  }
  
  implicit def tuple14Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14](t: (_ <: Ex[T1], _ <: Ex[T2], 
      _ <: Ex[T3], _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], 
      _ <: Ex[T10], _ <: Ex[T11], _ <: Ex[T12], _ <: Ex[T13], _ <: Ex[T14])) = {
    new Tu14Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14)
  }
  
  implicit def tuple15Expr[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15](t: (_ <: Ex[T1], _ <: Ex[T2], 
      _ <: Ex[T3], _ <: Ex[T4], _<: Ex[T5], _ <: Ex[T6], _ <: Ex[T7], _ <: Ex[T8], _ <: Ex[T9], 
      _ <: Ex[T10], _ <: Ex[T11], _ <: Ex[T12], _ <: Ex[T13], _ <: Ex[T14], _ <: Ex[T15])) = {
    new Tu15Ex[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
  }    
     
}

class Tuple2Expression[T1,T2](args: Ex[_]*) 
extends FactoryExpressionBase[(T1,T2)](classOf[Tuple2[T1,T2]], args:_*) {  
  def newInstance(args: AnyRef*): (T1,T2) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2]) 
  }     
}

class Tuple3Expression[T1,T2,T3](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple3[T1,T2,T3]](classOf[Tuple3[T1,T2,T3]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3])
  }   
}

class Tuple4Expression[T1,T2,T3,T4](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple4[T1,T2,T3,T4]](classOf[Tuple4[T1,T2,T3,T4]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], args(3).asInstanceOf[T4])
  }   
}

class Tuple5Expression[T1,T2,T3,T4,T5](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple5[T1,T2,T3,T4,T5]](classOf[Tuple5[T1,T2,T3,T4,T5]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5])
  }   
}

class Tuple6Expression[T1,T2,T3,T4,T5,T6](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple6[T1,T2,T3,T4,T5,T6]](classOf[Tuple6[T1,T2,T3,T4,T5,T6]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6])
  }   
}

class Tuple7Expression[T1,T2,T3,T4,T5,T6,T7](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple7[T1,T2,T3,T4,T5,T6,T7]](classOf[Tuple7[T1,T2,T3,T4,T5,T6,T7]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7])
  }   
}

class Tuple8Expression[T1,T2,T3,T4,T5,T6,T7,T8](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple8[T1,T2,T3,T4,T5,T6,T7,T8]](classOf[Tuple8[T1,T2,T3,T4,T5,T6,T7,T8]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8])
  }   
}

class Tuple9Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple9[T1,T2,T3,T4,T5,T6,T7,T8,T9]](classOf[Tuple9[T1,T2,T3,T4,T5,T6,T7,T8,T9]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9])
  }   
}

class Tuple10Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple10[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10]](classOf[Tuple10[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10])
  }   
}

class Tuple11Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple11[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11]](classOf[Tuple11[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10], args(10).asInstanceOf[T11])
  }   
}

class Tuple12Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple12[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12]](classOf[Tuple12[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10], args(10).asInstanceOf[T11], args(11).asInstanceOf[T12])
  }   
}

class Tuple13Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple13[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13]](classOf[Tuple13[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10], args(10).asInstanceOf[T11], args(11).asInstanceOf[T12],
     args(12).asInstanceOf[T13])
  }   
}

class Tuple14Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple14[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14]](classOf[Tuple14[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10], args(10).asInstanceOf[T11], args(11).asInstanceOf[T12],
     args(12).asInstanceOf[T13], args(13).asInstanceOf[T14])
  }   
}

class Tuple15Expression[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple15[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15]](classOf[Tuple15[T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15]], args:_*) {
  
  def newInstance(args: AnyRef*): (T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15) = {
    (args(0).asInstanceOf[T1], args(1).asInstanceOf[T2], args(2).asInstanceOf[T3], 
     args(3).asInstanceOf[T4], args(4).asInstanceOf[T5], args(5).asInstanceOf[T6],
     args(6).asInstanceOf[T7], args(7).asInstanceOf[T8], args(8).asInstanceOf[T9],
     args(9).asInstanceOf[T10], args(10).asInstanceOf[T11], args(11).asInstanceOf[T12],
     args(12).asInstanceOf[T13], args(13).asInstanceOf[T14], args(14).asInstanceOf[T15])
  }   
}

abstract class FactoryExpressionBase[T](cl: Class[T], args: Ex[_]*) 
  extends ExpressionBase[T](cl) with FactoryExpression[T] {
  
  def accept[R,C](v: Visitor[R,C], context: C): R = v.visit(this, context)
  
  def getArgs(): java.util.List[Ex[_]] = Arrays.asList(args:_*)
  
  def apply(i: Int): Ex[_] = args(i)
  
} 
