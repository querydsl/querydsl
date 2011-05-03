package com.mysema.query.scala

import com.mysema.codegen.CodeWriter
import com.mysema.codegen.ScalaWriter
import com.mysema.codegen.model._
import com.mysema.codegen.model.TypeCategory._

import com.mysema.query
import com.mysema.query.codegen._
import com.mysema.query.sql._
import com.mysema.query.sql.support._
import com.mysema.query.types._

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._
import scala.collection.mutable.Set

import javax.inject.Inject;

/**
 * EntitySerializer for Scala
 * 
 * @author tiwe
 *
 */
class ScalaEntitySerializer @Inject()(val typeMappings: TypeMappings) extends Serializer {
    
//  val typeMappings = ScalaTypeMappings.typeMappings

  val classHeaderFormat = "%1$s(cl: Class[_ <: %2$s], md: PathMetadata[_]) extends EntityPathImpl[%2$s](cl, md)"
  
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter]
    val simpleName: String = model.getSimpleName

    // package
    if (!model.getPackageName.isEmpty()) {
      writer.packageDecl(model.getPackageName)
    }

    // imports
    writer.importPackages("com.mysema.query.types","com.mysema.query.scala")
    writer.staticimports(classOf[PathMetadataFactory])

    var importedClasses = getAnnotationTypes(model)
    if (model.hasLists()) {
      importedClasses.add(classOf[List[_]].getName)
    }
    if (model.hasMaps()) {
      importedClasses.add(classOf[Map[_, _]].getName)
    }

    writer.importClasses(importedClasses.toArray: _*)
    
    writeHeader(model, scalaWriter)
    
    var modelName = writer.getRawName(model)
    
    writeAdditionalFields(model, scalaWriter)
    
    // additional constructors
    scalaWriter.line("def this(variable: String) = this(classOf[",modelName,"], forVariable(variable))\n")
    scalaWriter.line("def this(parent: Path[_], variable: String) = this(classOf[",modelName,"], forProperty(parent, variable))\n")

    // properties
    serializeProperties(model, writer, model.getProperties  )

    writeAdditionalProperties(model, scalaWriter)
    
    writer.end()
  }
  
  def writeAdditionalFields(model: EntityType, writer: ScalaWriter) {
      // override to customize
  }
  
  def writeAdditionalProperties(model: EntityType, writer: ScalaWriter) {
      // override to customize
  }
  
  def writeHeader(model: EntityType, writer: ScalaWriter) {
    val queryType = typeMappings.getPathType(model, model, true)
    val modelName = writer.getRawName(model)
    val queryTypeName = writer.getRawName(queryType)
    val classHeader = String.format(classHeaderFormat, queryTypeName, modelName)
        
    writer.beginObject(queryTypeName)
    writer.line("def as(variable: String) = new ", queryTypeName, "(variable)")
    writer.line("")
    writer.line("val ", model.getUncapSimpleName, " = as(\"",  model.getUncapSimpleName, "\")")
    writer.end()
    
    // header
    model.getAnnotations.foreach(writer.annotation(_))    
    writer.beginClass(classHeader)
  }

  private def serializeEntityProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) = {
    for (property <- properties if property.getType.getCategory == ENTITY) yield {
      val queryType = typeMappings.getPathType(property.getType, model, false)
      val typeName = writer.getRawName(queryType)
      val name = property.getEscapedName
      String.format("lazy val %1$s = new %2$s(this, \"%1$s\")", name, typeName)
    }
  }
  
  private def serializeOtherProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) = {
    for (property <- properties if property.getType.getCategory != ENTITY) yield { 
      val methodName: String = property.getType.getCategory match {
        case ARRAY => "createArray"
        case BOOLEAN => "createBoolean"  
        case COLLECTION => "createCollection"
        case COMPARABLE => "createComparable"        
        case DATE => "createDate"
        case DATETIME => "createDateTime"
        case ENUM => "createEnum"
        case LIST => "createList"
        case MAP => "createMap"
        case NUMERIC => "createNumber"
        case SET => "createSet"
        case SIMPLE => "createSimple"
        case STRING => "createString"
        case TIME => "createTime"     
      }
      
      val value = property.getType.getCategory match {
        case BOOLEAN | STRING => methodName + "(\"" + property.getName + "\")"
        case LIST | SET | COLLECTION => {
          val componentType = writer.getGenericName(true, property.getParameter(0))
          val queryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false)
          methodName + "(\"" + property.getName + "\", classOf[" + componentType + "], classOf[" + writer.getGenericName(true, queryType) + "])"
        }
        case MAP => {
            val keyType = writer.getGenericName(true, property.getParameter(0))
            val valueType = writer.getGenericName(true, property.getParameter(1))
            val queryType = typeMappings.getPathType(getRaw(property.getParameter(1)), model, false)
            methodName + "(\"" + property.getName + "\", classOf[" + keyType + "], classOf[" + valueType + "], classOf[" + writer.getGenericName(true, queryType) + "])"      
        }
        case _ => methodName + "(\"" + property.getName + "\", classOf[" + writer.getRawName(property.getType) + "])"
      }
      (property.getEscapedName, value)
    }
  }
  
  def serializeProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) {
    // entity properties
    serializeEntityProperties(model, writer, properties) foreach (writer.line(_, "\n"))
    
    // other properties
    serializeOtherProperties(model, writer, properties) foreach { case (propertyName, value) =>
      writer.line("val ", propertyName, " = ", value, "\n")
    }
  }

  def getAnnotationTypes(model: EntityType): Set[String] = {
    Set() ++ (model.getAnnotations.map(_.annotationType.getName))
  }
  
  private def getRaw(t : Type): Type = {
    t match {
      case entityType: EntityType if entityType.getPackageName.startsWith("java") => t
      case _ => new SimpleType(t, t.getParameters)
    }
  }

}