package com.querydsl.scala

import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.querydsl.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class ScalaBeanSerializerTest extends CompileTestUtils {

  val typeMappings = ScalaTypeMappings.create
  
  var writer = new StringWriter()

  val entityType = EntityTypes.entityType

  @Test
  def Print {
    val serializer = new ScalaBeanSerializer(typeMappings)
    serializer.javaBeanSupport = true
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    
    //println(writer.toString)
    
    var toMatch = """package com.querydsl
    import scala.reflect.BeanProperty
    import java.util.List
    import java.util.Map
    /**
    * DomainClass is a Querydsl bean type
    */
    class DomainClass {
    @BeanProperty var arrayField: Array[String] = _
    @BeanProperty var boolean$: java.lang.Boolean = _
    @BeanProperty var collection: java.util.Collection[DomainClass] = _
    @BeanProperty var date: java.util.Date = _
    @BeanProperty var entityField: DomainClass = _
    @BeanProperty var integer: Integer = _
    @BeanProperty var listField: List[DomainClass] = _
    @BeanProperty var mapField: Map[DomainClass, DomainClass] = _
    @BeanProperty var setField: java.util.Set[DomainClass] = _
    @BeanProperty var time: java.sql.Time = _"""
    
    val str = writer.toString().replaceAll("\\s+", " ")
    //println(str)
 
    toMatch.split("\\n").map(_.trim).foreach { line =>
      assertTrue(line, str.contains(line));
    }
    
  }

  @Test
  def Compile {
    val serializer = new ScalaBeanSerializer(typeMappings)
    serializer.createCompanionObject = false
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    val str = writer.toString()
    assertCompileSuccess(str)
  }
}