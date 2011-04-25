package com.mysema.query.scala;

import scala.collection.mutable.ListBuffer

import org.junit.{ Test, Before, After, Ignore };
import org.junit.Assert._;

@Ignore
class PlanetTest {
    
  @Test
  def Enum_valueOf {
    assertEquals(Planet.MERCURY, Enum.valueOf(classOf[Planet], "MERCURY"))
    assertEquals(Planet.VENUS, Enum.valueOf(classOf[Planet], "VENUS"))
    assertEquals(Planet.PLUTO, Enum.valueOf(classOf[Planet], "PLUTO"))
  }
  
  @Test
  def Enum_ordinal {
    assertEquals(0, Planet.MERCURY.ordinal)  
    assertEquals(1, Planet.VENUS.ordinal)  
    assertEquals(2, Planet.PLUTO.ordinal)
  }
  
  @Test
  def Enum_name {
    assertEquals("MERCURY", Planet.MERCURY.name)
    assertEquals("VENUS", Planet.VENUS.name)
    assertEquals("PLUTO", Planet.PLUTO.name)
  }
  
}

object Planet extends Enums[Planet]{
  val MERCURY = create(3.303e+23, 2.4397e6)
  val VENUS  = create(4.869e+24, 6.0518e6)
  val EARTH = create(5.976e+24, 6.37814e6)
  val MARS = create(6.421e+23, 3.3972e6)
  val JUPITER = create(1.9e+27,   7.1492e7)
  val SATURN = create(5.688e+26, 6.0268e7)
  val URANUS = create(8.686e+25, 2.5559e7)
  val NEPTUNE = create(1.024e+26, 2.4746e7)
  val PLUTO = create(1.27e+22,  1.137e6)
  val values = getValues
}

class Planet(n: String, o: Int, val mass: Double, val radius: Double) extends Enum[Planet](n, o) { 
  private val G = 6.67300E-11
  def surfaceGravity = G * mass / (radius * radius)
  def surfaceWeight(otherMass: Double) = otherMass * surfaceGravity      
}

class Enums[T <: Enum[T]](implicit val t: Manifest[T]) {
   private var i = 0
   private val enumType: Class[T] = t.erasure.asInstanceOf[Class[T]]
   private val enumConstructor = enumType.getConstructors()(0)
   private val enumNames = enumType.getDeclaredFields filter {_.getType eq enumType} map {_.getName}
   private val enums = new ListBuffer[T]()
   
   def create(a: Any*): T = {
     val enum = enumConstructor.newInstance((enumNames(i) :: i :: a :: Nil) toArray).asInstanceOf[T]
     i = i+1
     enums += enum     
     enum 
   }

   def getValues(): Array[T] = enums.toArray
}
