package com.mysema.query.scala

import com.mysema.query.types._

object TypeDefs {
  
  type Ex[T] = Expression[T]
  
  type Tu2Ex[T1,T2] = Tuple2Expression[T1,T2]
  
  type Tu3Ex[T1,T2,T3] = Tuple3Expression[T1,T2,T3]
  
  type Tu4Ex[T1,T2,T3,T4] = Tuple4Expression[T1,T2,T3,T4]
  
  type Tu5Ex[T1,T2,T3,T4,T5] = Tuple5Expression[T1,T2,T3,T4,T5]
  
  type JavaList[T] = java.util.List[T]
  
}