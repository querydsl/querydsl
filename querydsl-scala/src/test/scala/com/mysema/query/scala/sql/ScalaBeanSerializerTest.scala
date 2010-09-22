package com.mysema.query.scala.sql

import org.apache.commons.lang.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.mysema.query.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class ScalaBeanSerializerTest {

  var entityType: EntityType = null;

  var writer = new StringWriter();

  @Before
  def setUp() {
    // type
    val typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false, false);
    entityType = new EntityType("Q", typeModel);

    // property
    entityType.addProperty(new Property(entityType, "entityField", entityType, new Array[String](0)));
    entityType.addProperty(new Property(entityType, "collection", new SimpleType(Types.COLLECTION, typeModel), new Array[String](0)));
    entityType.addProperty(new Property(entityType, "listField", new SimpleType(Types.LIST, typeModel), new Array[String](0)));
    entityType.addProperty(new Property(entityType, "setField", new SimpleType(Types.SET, typeModel), new Array[String](0)));
    entityType.addProperty(new Property(entityType, "arrayField", new ClassType(TypeCategory.ARRAY, classOf[Array[String]]), new Array[String](0)));
    entityType.addProperty(new Property(entityType, "mapField", new SimpleType(Types.MAP, typeModel, typeModel), new Array[String](0)));

    List(classOf[Boolean], classOf[Comparable[_]], classOf[Integer], classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])
      .foreach(cl => {
        var classType = new ClassType(TypeCategory.get(cl.getName), cl);
        entityType.addProperty(new Property(entityType, StringUtils.uncapitalize(cl.getSimpleName), classType, new Array[String](0)));
      })

    // constructor
    val firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING, classOf[String]));
    val lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING, classOf[String]));
    entityType.addConstructor(new Constructor(List(firstName, lastName)));

    // method
    val method = new Method(Types.STRING, "method", "abc", Types.STRING);
    entityType.addMethod(method);
  }

  @Test
  def Print() {
    val serializer = new com.mysema.query.scala.sql.ScalaBeanSerializer();
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer));
    val str = writer.toString();
    System.err.println(str);
  }
}