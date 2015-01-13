package com.querydsl.scala

import com.mysema.codegen.model._
import com.querydsl.codegen._
import com.google.common.base.CaseFormat

import scala.collection.JavaConversions._

object EntityTypes {

    val typeModel = new SimpleType(TypeCategory.ENTITY, 
        "com.querydsl.DomainClass", "com.querydsl", "DomainClass", false, false)
    val entityType = new EntityType(typeModel)

    // properties
    for ( (name, clazz) <- List( 
        ("entityField",entityType), 
        ("collection", new SimpleType(Types.COLLECTION, typeModel)),
        ("listField",new SimpleType(Types.LIST, typeModel)), ("setField",new SimpleType(Types.SET, typeModel)),
        ("arrayField", new ClassType(TypeCategory.ARRAY, classOf[Array[String]])),
        ("mapField", new SimpleType(Types.MAP, typeModel, typeModel)))) {
      entityType.addProperty(new Property(entityType, name, clazz))
    }
    
    for ( clazz <- List(
        classOf[java.lang.Boolean], classOf[Boolean], classOf[Array[Byte]],
        classOf[Integer], classOf[java.util.Date], classOf[java.sql.Date], classOf[java.sql.Time])) {
      val name = uncapitalize(clazz.getSimpleName + 
          (if (clazz.isPrimitive) "_p" else "")).replace("[","").replace("]","")
      entityType.addProperty(new Property(entityType, name, 
          new ClassType(TypeCategory.get(clazz.getName), clazz)))
    }
    
    // constructor
    val firstName = new Parameter("firstName", Types.STRING)
    val lastName = new Parameter("lastName", Types.STRING)
    
    entityType.addConstructor(new Constructor(List(firstName, lastName)))
    
    def uncapitalize(str: String) = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, str)

}