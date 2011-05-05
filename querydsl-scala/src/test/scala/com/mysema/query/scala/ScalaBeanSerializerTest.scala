package com.mysema.query.scala

import org.apache.commons.lang.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.mysema.query.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class ScalaBeanSerializerTest {

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

    List(classOf[java.lang.Boolean], classOf[Integer], classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])
      .foreach(cl => {
        var classType = new ClassType(TypeCategory.get(cl.getName), cl)
        entityType.addProperty(new Property(entityType, StringUtils.uncapitalize(cl.getSimpleName), classType, new Array[String](0)))
      })

    // constructor
    val firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING, classOf[String]))
    val lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING, classOf[String]))
    entityType.addConstructor(new Constructor(List(firstName, lastName)))
  }

  @Test
  def Print {
    val serializer = new ScalaBeanSerializer()
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    
    println(writer.toString)
    
    val str = writer.toString().replaceAll("\\s+", " ")
    
    assertTrue(str.contains("package com.mysema.query"))
    assertTrue(str.contains("import scala.reflect.BeanProperty"))
    assertTrue(str.contains("import java.util.List"))
    assertTrue(str.contains("import java.util.Map"))
    assertTrue(str.contains("/**"))
    assertTrue(str.contains("* DomainClass is a Querydsl bean type"))
    assertTrue(str.contains("*/"))
    assertTrue(str.contains("class DomainClass {"))
    assertTrue(str.contains("@BeanProperty var arrayField: Array[String] = _"))
    assertTrue(str.contains("@BeanProperty var boolean$: java.lang.Boolean = _"))
    assertTrue(str.contains("@BeanProperty var collection: java.util.Collection[DomainClass] = _"))
    assertTrue(str.contains("@BeanProperty var date: java.util.Date = _"))
    assertTrue(str.contains("@BeanProperty var entityField: DomainClass = _"))
    assertTrue(str.contains("@BeanProperty var integer: Integer = _"))
    assertTrue(str.contains("@BeanProperty var listField: List[DomainClass] = _"))
    assertTrue(str.contains("@BeanProperty var mapField: Map[DomainClass, DomainClass] = _"))
    assertTrue(str.contains("@BeanProperty var setField: java.util.Set[DomainClass] = _"))
    assertTrue(str.contains("@BeanProperty var time: java.sql.Time = _"))
    assertTrue(str.contains("}"))
  }
}