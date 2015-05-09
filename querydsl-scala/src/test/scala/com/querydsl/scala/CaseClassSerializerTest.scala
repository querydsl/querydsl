package com.querydsl.scala

import java.io.StringWriter

import com.mysema.codegen._
import com.querydsl.codegen._
import org.junit._

class CaseClassSerializerTest {

  val typeMappings = ScalaTypeMappings.create
  
  var entityType = EntityTypes.entityType

  var writer = new StringWriter()
  
  @Test
  def Print {
    val serializer = new CaseClassSerializer(typeMappings)
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    
    //println(writer.toString)
  }
  
  @Test
  def Compile {
    val serializer = new CaseClassSerializer(typeMappings)
    serializer.createCompanionObject = false
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    val str = writer.toString
    
    CompileTestUtils.assertCompileSuccess(str)
  }  
  
}  
    