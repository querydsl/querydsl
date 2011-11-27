package com.mysema.query.scala

import com.mysema.query.types._

object TypeDefs {
  
  type Ex[T] = Expression[T]
  
  type Tu2Ex[T1,T2,E1 <: Ex[T1],E2 <: Ex[T2]] = Tuple2Expression[T1,T2,E1,E2]
  
  type Tu3Ex[T1,T2,T3,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3]] = Tuple3Expression[T1,T2,T3,E1,E2,E3]
  
  type Tu4Ex[T1,T2,T3,T4,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4]] = Tuple4Expression[T1,T2,T3,T4,E1,E2,E3,E4]
  
  type Tu5Ex[T1,T2,T3,T4,T5,E1 <: Ex[T1],E2 <: Ex[T2],E3 <: Ex[T3],E4 <: Ex[T4], E5 <: Ex[T5]] = Tuple5Expression[T1,T2,T3,T4,T5,E1,E2,E3,E4,E5]
  
  type JavaList[T] = java.util.List[T]
  
}