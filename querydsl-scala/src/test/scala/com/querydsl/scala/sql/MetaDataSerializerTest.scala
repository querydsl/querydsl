package com.querydsl.scala.sql

import com.mysema.codegen._;
import com.mysema.codegen.model._;

import com.querydsl.codegen._;
import com.querydsl.sql._
import com.querydsl.sql.codegen._

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

import com.querydsl.scala._

class ScalaMetaDataSerializerTest {

  var entityType: EntityType = null

  val writer = new StringWriter()

  @Before
  def setUp() {
    // type
    val typeModel = new SimpleType(TypeCategory.ENTITY, 
        "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false)
    entityType = new EntityType(typeModel)
    //entityType.addAnnotation(new TableImpl("DOMAIN_TYPE"))
    entityType.getData().put("table", "DOMAIN_TYPE")

    // properties
    List(classOf[java.lang.Boolean], classOf[Comparable[_]], classOf[Integer], 
         classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])
      .foreach(cl => {
        val classType = new ClassType(TypeCategory.get(cl.getName), cl)
        val propertyName = StringUtils.uncapitalize(cl.getSimpleName)
        var property = new Property(entityType, propertyName, classType)
        property.getData.put("COLUMN", ColumnMetadata.named(propertyName.toUpperCase).ofType(0))
        entityType.addProperty(property)
      })
  }

  @Test
  def Print {
    val typeMappings = ScalaTypeMappings.create
    val namingStrategy = new DefaultNamingStrategy()
    val serializer = new ScalaMetaDataSerializer(typeMappings, namingStrategy)
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    val str = writer.toString()
    //System.err.println(str)
    assertTrue("companion object isn't before class", str.indexOf("object") < str.indexOf("class"))
    //assertTrue("companion object isn't before annotations", str.indexOf("object") < str.indexOf("@Table"))    
  }
  
}