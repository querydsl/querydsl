package com.mysema.public_

class DummyClass

object Num {
  implicit val int = new Num[Int]
  implicit val integer = new Num[java.lang.Integer]
  implicit val long = new Num[Long]
}

class Num[T]

object Methods {
  def number[T : Num](t: T) = t
}

class Usage {
  def method(i: java.lang.Integer) = Methods.number(i)
}
