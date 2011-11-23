package com.mysema.query.scala

import org.apache.commons.lang3.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.mysema.query.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class CaseClassSerializerTest extends CompileTestUtils {

  val typeMappings = ScalaTypeMappings.create
  
  var entityType: EntityType = null

  var writer = new StringWriter()

  @Before
  def setUp() {
    // type
    val typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false, false)
    entityType = new EntityType(typeModel)

    // property
    entityType.addProperty(new Property(entityType, "entityField", entityType, new Array[String](0)))
    entityType.addProperty(new Property(entityType, "collection", new SimpleType(Types.COLLECTION, typeModel), new Array[String](0)))
    entityType.addProperty(new Property(entityType, "listField", new SimpleType(Types.LIST, typeModel), new Array[String](0)))
    entityType.addProperty(new Property(entityType, "setField", new SimpleType(Types.SET, typeModel), new Array[String](0)))
    entityType.addProperty(new Property(entityType, "arrayField", new ClassType(TypeCategory.ARRAY, classOf[Array[String]]), new Array[String](0)))
    entityType.addProperty(new Property(entityType, "mapField", new SimpleType(Types.MAP, typeModel, typeModel), new Array[String](0)))
  }
  
  @Test
  def Print {
    val serializer = new CaseClassSerializer(typeMappings)
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    
    println(writer.toString)
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
    