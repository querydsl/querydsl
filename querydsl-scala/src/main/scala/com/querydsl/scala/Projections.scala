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

package com.querydsl.scala

import com.querydsl.core.types._
import com.querydsl.core.types.{ Projections => ProjectionsFactory }
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
  def create[T](expressions: Ex[_]*)(implicit mf: Manifest[T]): ExpressionBase[_] = {
    val clazz = mf.runtimeClass.asInstanceOf[Class[T]]
    try {
      clazz.getConstructor()
      ProjectionsFactory.fields[T](clazz, expressions:_*)
    } catch { case e: NoSuchMethodException => {
      ProjectionsFactory.constructor[T](clazz, expressions:_*)   
    }}    
  }
  
  implicit def tuple2Expr[A,B](t: (_ <: Ex[A], _ <: Ex[B])) = new Tu2Ex[A,B](t._1, t._2) 

  implicit def tuple3Expr[A,B,C](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C])) = {
    new Tu3Ex[A,B,C](t._1, t._2, t._3) 
  }

  implicit def tuple4Expr[A,B,C,D](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], _ <: Ex[D])) = {
    new Tu4Ex[A,B,C,D](t._1, t._2, t._3, t._4)
  }
  
  implicit def tuple5Expr[A,B,C,D,E](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], _ <: Ex[D], 
      _<: Ex[E])) = {
    new Tu5Ex[A,B,C,D,E](t._1, t._2, t._3, t._4, t._5)
  }
  
  implicit def tuple6Expr[A,B,C,D,E,F](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], _ <: Ex[D], 
      _<: Ex[E], _ <: Ex[F])) = {
    new Tu6Ex[A,B,C,D,E,F](t._1, t._2, t._3, t._4, t._5, t._6)
  }
  
  implicit def tuple7Expr[A,B,C,D,E,F,G](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], _ <: Ex[D], 
      _<: Ex[E], _ <: Ex[F], _ <: Ex[G])) = {
    new Tu7Ex[A,B,C,D,E,F,G](t._1, t._2, t._3, t._4, t._5, t._6, t._7)
  }
  
  implicit def tuple8Expr[A,B,C,D,E,F,G,H](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], _ <: Ex[D], 
      _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H])) = {
    new Tu8Ex[A,B,C,D,E,F,G,H](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8)
  }
  
  implicit def tuple9Expr[A,B,C,D,E,F,G,H,I](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C],
      _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I])) = {
    new Tu9Ex[A,B,C,D,E,F,G,H,I](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
  }
  
  implicit def tuple10Expr[A,B,C,D,E,F,G,H,I,J](t: (_ <: Ex[A], _ <: Ex[B], _ <: Ex[C], 
      _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], _ <: Ex[J])) = {
    new Tu10Ex[A,B,C,D,E,F,G,H,I,J](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10)
  }
  
  implicit def tuple11Expr[A,B,C,D,E,F,G,H,I,J,K](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K])) = {
    new Tu11Ex[A,B,C,D,E,F,G,H,I,J,K](t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, 
        t._9, t._10, t._11)
  }
  
  implicit def tuple12Expr[A,B,C,D,E,F,G,H,I,J,K,L](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L])) = {
    new Tu12Ex[A,B,C,D,E,F,G,H,I,J,K,L](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12)
  }
  
  implicit def tuple13Expr[A,B,C,D,E,F,G,H,I,J,K,L,M](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M])) = {
    new Tu13Ex[A,B,C,D,E,F,G,H,I,J,K,L,M](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13)
  }
  
  implicit def tuple14Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N])) = {
    new Tu14Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14)
  }
  
  implicit def tuple15Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O])) = {
    new Tu15Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15)
  }    
  
  implicit def tuple16Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O],
      _ <: Ex[P])) = {
    new Tu16Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16)
  }
  
  implicit def tuple17Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O],
      _ <: Ex[P], _ <: Ex[Q])) = {
    new Tu17Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17)
  }
  
  implicit def tuple18Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O],
      _ <: Ex[P], _ <: Ex[Q], _ <: Ex[R])) = {
    new Tu18Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18)
  }
  
  implicit def tuple19Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O],
      _ <: Ex[P], _ <: Ex[Q], _ <: Ex[R], _ <: Ex[S])) = {
    new Tu19Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19)
  }
  
  implicit def tuple20Expr[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T](t: (_ <: Ex[A], _ <: Ex[B], 
      _ <: Ex[C], _ <: Ex[D], _<: Ex[E], _ <: Ex[F], _ <: Ex[G], _ <: Ex[H], _ <: Ex[I], 
      _ <: Ex[J], _ <: Ex[K], _ <: Ex[L], _ <: Ex[M], _ <: Ex[N], _ <: Ex[O],
      _ <: Ex[P], _ <: Ex[Q], _ <: Ex[R], _ <: Ex[S], _ <: Ex[T])) = {
    new Tu20Ex[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T](t._1, t._2, t._3, t._4, t._5, t._6, t._7, 
        t._8, t._9, t._10, t._11, t._12, t._13, t._14, t._15, t._16, t._17, t._18, t._19, t._20)
  }
       
}

class Tuple2Expression[A,B](args: Ex[_]*) 
extends FactoryExpressionBase[(A,B)](classOf[Tuple2[A,B]], args:_*) {  
  def newInstance(args: AnyRef*): (A,B) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B]) 
  }     
}

class Tuple3Expression[A,B,C](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple3[A,B,C]](classOf[Tuple3[A,B,C]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C])
  }   
}

class Tuple4Expression[A,B,C,D](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple4[A,B,C,D]](classOf[Tuple4[A,B,C,D]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], args(3).asInstanceOf[D])
  }   
}

class Tuple5Expression[A,B,C,D,E](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple5[A,B,C,D,E]](classOf[Tuple5[A,B,C,D,E]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E])
  }   
}

class Tuple6Expression[A,B,C,D,E,F](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple6[A,B,C,D,E,F]](classOf[Tuple6[A,B,C,D,E,F]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F])
  }   
}

class Tuple7Expression[A,B,C,D,E,F,G](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple7[A,B,C,D,E,F,G]](classOf[Tuple7[A,B,C,D,E,F,G]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G])
  }   
}

class Tuple8Expression[A,B,C,D,E,F,G,H](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple8[A,B,C,D,E,F,G,H]](classOf[Tuple8[A,B,C,D,E,F,G,H]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H])
  }   
}

class Tuple9Expression[A,B,C,D,E,F,G,H,I](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple9[A,B,C,D,E,F,G,H,I]](classOf[Tuple9[A,B,C,D,E,F,G,H,I]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I])
  }   
}

class Tuple10Expression[A,B,C,D,E,F,G,H,I,J](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple10[A,B,C,D,E,F,G,H,I,J]](classOf[Tuple10[A,B,C,D,E,F,G,H,I,J]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J])
  }   
}

class Tuple11Expression[A,B,C,D,E,F,G,H,I,J,K](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple11[A,B,C,D,E,F,G,H,I,J,K]](classOf[Tuple11[A,B,C,D,E,F,G,H,I,J,K]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K])
  }   
}

class Tuple12Expression[A,B,C,D,E,F,G,H,I,J,K,L](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple12[A,B,C,D,E,F,G,H,I,J,K,L]](classOf[Tuple12[A,B,C,D,E,F,G,H,I,J,K,L]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L])
  }   
}

class Tuple13Expression[A,B,C,D,E,F,G,H,I,J,K,L,M](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple13[A,B,C,D,E,F,G,H,I,J,K,L,M]](classOf[Tuple13[A,B,C,D,E,F,G,H,I,J,K,L,M]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M])
  }   
}

class Tuple14Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple14[A,B,C,D,E,F,G,H,I,J,K,L,M,N]](classOf[Tuple14[A,B,C,D,E,F,G,H,I,J,K,L,M,N]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N])
  }   
}

class Tuple15Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple15[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O]](classOf[Tuple15[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O])
  }   
}

class Tuple16Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple16[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P]](classOf[Tuple16[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O],
     args(15).asInstanceOf[P])
  }   
}

class Tuple17Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple17[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q]](classOf[Tuple17[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O],
     args(15).asInstanceOf[P], args(16).asInstanceOf[Q])
  }   
}

class Tuple18Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple18[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R]](classOf[Tuple18[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O],
     args(15).asInstanceOf[P], args(16).asInstanceOf[Q], args(17).asInstanceOf[R])
  }   
}

class Tuple19Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple19[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S]](classOf[Tuple19[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O],
     args(15).asInstanceOf[P], args(16).asInstanceOf[Q], args(17).asInstanceOf[R],
     args(18).asInstanceOf[S])
  }   
}

class Tuple20Expression[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T](args: Ex[_]*) 
extends FactoryExpressionBase[Tuple20[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T]](classOf[Tuple20[A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T]], args:_*) {
  
  def newInstance(args: AnyRef*): (A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T) = {
    (args(0).asInstanceOf[A], args(1).asInstanceOf[B], args(2).asInstanceOf[C], 
     args(3).asInstanceOf[D], args(4).asInstanceOf[E], args(5).asInstanceOf[F],
     args(6).asInstanceOf[G], args(7).asInstanceOf[H], args(8).asInstanceOf[I],
     args(9).asInstanceOf[J], args(10).asInstanceOf[K], args(11).asInstanceOf[L],
     args(12).asInstanceOf[M], args(13).asInstanceOf[N], args(14).asInstanceOf[O],
     args(15).asInstanceOf[P], args(16).asInstanceOf[Q], args(17).asInstanceOf[R],
     args(18).asInstanceOf[S], args(19).asInstanceOf[T])
  }   
}

abstract class FactoryExpressionBase[T](cl: Class[T], args: Ex[_]*) 
  extends ExpressionBase[T](cl) with FactoryExpression[T] {
  
  def accept[R,C](v: Visitor[R,C], context: C): R = v.visit(this, context)
  
  def getArgs(): java.util.List[Ex[_]] = Arrays.asList(args:_*)
  
  def apply(i: Int): Ex[_] = args(i)
  
} 
