package com.querydsl.scala

import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.querydsl.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class CaseClassSerializerTest extends CompileTestUtils {

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
    val str = writer.toString()
    
    assertCompileSuccess(str)
  }  
  
}  
    