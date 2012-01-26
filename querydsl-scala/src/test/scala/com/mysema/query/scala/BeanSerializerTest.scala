package com.mysema.query.scala

import org.apache.commons.lang3.StringUtils
import com.mysema.codegen._;
import com.mysema.codegen.model._;
import com.mysema.query.codegen._;

import java.io.StringWriter;

import org.junit._
import org.junit.Assert._

import scala.collection.JavaConversions._

class ScalaBeanSerializerTest extends CompileTestUtils {

  val typeMappings = ScalaTypeMappings.create
  
  var entityType: EntityType = null

  var writer = new StringWriter()

  @Before
  def setUp() {
    // type
    val typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false, false)
    entityType = new EntityType(typeModel)

    // properties
    for ( (name, clazz) <- List( ("entityField",entityType), ("collection", new SimpleType(Types.COLLECTION, typeModel)),
                                 ("listField",new SimpleType(Types.LIST, typeModel)), ("setField",new SimpleType(Types.SET, typeModel)),
                                 ("arrayField", new ClassType(TypeCategory.ARRAY, classOf[Array[String]])),
                                 ("mapField", new SimpleType(Types.MAP, typeModel, typeModel)))) {
      entityType.addProperty(new Property(entityType, name, clazz, new Array[String](0)))
    }
    
    for ( clazz <- List(classOf[java.lang.Boolean], classOf[Boolean], classOf[Array[Byte]],
                        classOf[Integer], classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])) {
      val name = StringUtils.uncapitalize(clazz.getSimpleName + (if (clazz.isPrimitive) "_p" else "")).replace("[","").replace("]","")
      entityType.addProperty(new Property(entityType, name, new ClassType(TypeCategory.get(clazz.getName), clazz), new Array[String](0)))
    }
    // constructor
    val firstName = new Parameter("firstName", Types.STRING)
    val lastName = new Parameter("lastName", Types.STRING)
    entityType.addConstructor(new Constructor(List(firstName, lastName)))
  }

  @Test
  def Print {
    val serializer = new ScalaBeanSerializer(typeMappings)
    serializer.javaBeanSupport = true
    typeMappings.register(entityType, new QueryTypeFactoryImpl("Q", "", "").create(entityType))
    serializer.serialize(entityType, SimpleSerializerConfig.DEFAULT, new ScalaWriter(writer))
    
    println(writer.toString)
    
    // TODO : simplify
    val str = writer.toString().replaceAll("\\s+", " ")
    println(str)
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