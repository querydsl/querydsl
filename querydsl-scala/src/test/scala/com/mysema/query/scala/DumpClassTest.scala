package com.mysema.query.scala

import com.mysema.query.jpa.impl.JPAQuery
import com.mysema.query.scala.Conversions._

import com.mysema.query.annotations._

import javax.persistence._;
import org.junit.{ Test, Before, After };
import org.junit.Assert._

import scala.collection.JavaConversions._

import scala.reflect.BeanProperty;

class DumpClassTest {

  @Test
  def test() {
    List(classOf[Class1], classOf[Class2]).foreach(cl =>
      {
        println(cl.getName);
        cl.getDeclaredFields.foreach(f => {
          val annotations = java.util.Arrays.asList(f.getAnnotations: _*).map("@" + _.annotationType.getSimpleName).mkString(" ");
          println(" field " + annotations + " " + f.getName);
        });
        cl.getDeclaredMethods.foreach(m => {          
          val annotations = java.util.Arrays.asList(m.getReturnType.getAnnotations: _*).map("@" + _.annotationType.getSimpleName).mkString(" ");
          println(" method " + annotations + " " + m.getName);
        });
        println();
      });
  }
}

@QueryEntity
class Class1(
  @BeanProperty @Id var id: Integer,
  @BeanProperty var str: String,
  @BeanProperty @ManyToOne var manyToOne: Class1)

@QueryEntity
class Class2 {
  @BeanProperty
  @Id
  var id: Integer = _;
  @BeanProperty
  var str: Class2 = _;
  @BeanProperty
  @ManyToOne
  var manyToOne: Class2 = _
}
