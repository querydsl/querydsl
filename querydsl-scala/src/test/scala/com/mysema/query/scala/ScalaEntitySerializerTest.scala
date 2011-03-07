package com.mysema.query.scala

import com.mysema.query.types._;

import org.junit.{ Test, Before, After, Assert };
import org.junit.Assert._

import org.apache.commons.lang.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;

import com.mysema.query.codegen._;
import com.mysema.query.sql._

import java.io.StringWriter;


class ScalaEntitySerializerTest {
    
  var entityType: EntityType = null

  val writer = new StringWriter()
  
  @Before
  def setUp() {
    val typeModel = new ClassType(TypeCategory.ENTITY, classOf[Person])
    entityType = new EntityType(typeModel)
    entityType.addProperty(new Property(entityType, "scalaInt", Types.INTEGER))
    entityType.addProperty(new Property(entityType, "javaInt", Types.INTEGER))
    entityType.addProperty(new Property(entityType, "javaDouble", Types.DOUBLE))
    entityType.addProperty(new Property(entityType, "firstName", Types.STRING))
    entityType.addProperty(new Property(entityType, "lastName", Types.STRING))
    entityType.addProperty(new Property(entityType, "scalaList", new SimpleType(Types.LIST, Types.STRING)))
    entityType.addProperty(new Property(entityType, "scalaMap", new SimpleType(Types.MAP, Types.STRING, Types.STRING)))
    entityType.addProperty(new Property(entityType, "javaCollection", new SimpleType(Types.COLLECTION, Types.STRING)))
    entityType.addProperty(new Property(entityType, "javaSet", new SimpleType(Types.SET, Types.STRING)))
    entityType.addProperty(new Property(entityType, "javaList", new SimpleType(Types.LIST, Types.STRING)))
    entityType.addProperty(new Property(entityType, "javaMap", new SimpleType(Types.MAP, Types.STRING, Types.STRING)))
    entityType.addProperty(new Property(entityType, "listOfPersons", new SimpleType(Types.LIST, entityType)))
    entityType.addProperty(new Property(entityType, "array", new ClassType(TypeCategory.ARRAY, classOf[Array[String]])))
    entityType.addProperty(new Property(entityType, "other", entityType))
    
  }

  @Test
  def Print() {
    val typeMappings = ScalaTypeMappings.create
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q","","").create(entityType))
    val serializer = new ScalaEntitySerializer(typeMappings)
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    val str = writer.toString()
    System.err.println(str)
    assertTrue(str.contains("class QPerson(cl: Class[_ <: Person], md: PathMetadata[_]) extends EntityPathImpl[Person](cl, md) {"))
    assertTrue(str.contains("def this(variable: String) = this(classOf[Person], forVariable(variable))"))
    assertTrue(str.contains("def this(parent: Path[_], variable: String) = this(classOf[Person], forProperty(parent, variable))"))
    System.err.println(str)    
  }
    
}