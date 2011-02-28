package com.mysema.query.scala.sql

import org.apache.commons.lang.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;

import com.mysema.query.codegen._;
import com.mysema.query.sql._

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class ScalaMetaDataSerializerTest {

  var entityType: EntityType = null;

  val writer = new StringWriter();

  @Before
  def setUp() {
    // type
    val typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false, false);
    entityType = new EntityType("Q", "", typeModel);
    entityType.addAnnotation(new TableImpl("DOMAIN_TYPE"));

    // properties
    List(classOf[java.lang.Boolean], classOf[Comparable[_]], classOf[Integer], classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])
      .foreach(cl => {
        var classType = new ClassType(TypeCategory.get(cl.getName), cl);
        entityType.addProperty(new Property(entityType, StringUtils.uncapitalize(cl.getSimpleName), classType, new Array[String](0)));
      })
  }

  @Test
  def Print {
    val namingStrategy = new DefaultNamingStrategy();
    val serializer = new ScalaMetaDataSerializer("Q", "", namingStrategy);
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer));
    val str = writer.toString();
    assertTrue("companion object isn't before class", str.indexOf("object") < str.indexOf("class"));
    assertTrue("companion object isn't before annotations", str.indexOf("object") < str.indexOf("@Table"));
    System.err.println(str);    
  }
  
}