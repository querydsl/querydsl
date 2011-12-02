package com.mysema.query.scala

import com.mysema.query.codegen._
import com.mysema.codegen.{ CodeWriter, ScalaWriter }
import com.mysema.codegen.model.{ Parameter, Type }
import com.mysema.query

import javax.inject.Inject

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._
import scala.collection.mutable.Set

/**
 * @author tiwe
 *
 */
class ScalaBeanSerializer @Inject() (typeMappings: TypeMappings) extends BeanSerializer(typeMappings) {

  var javadocSuffix = " is a Querydsl bean type"
  
  var javaBeanSupport = false
    
  def writeClass(model: EntityType, writer: ScalaWriter) = {
    // javadoc        
    writer.javadoc(model.getSimpleName + javadocSuffix)
    
    // header
    model.getAnnotations foreach(writer.annotation(_))
    writer.beginClass(model)

    // properties
    model.getProperties foreach { property =>
      property.getAnnotations.foreach(writer.annotation(_))
      if (javaBeanSupport) writer.line("@BeanProperty")
      writer.publicField(property.getType(), property.getEscapedName, "_")
    }

    writer.end()
  }
  
}

/**
 * @author tiwe
 *
 */
class CaseClassSerializer @Inject() (typeMappings: TypeMappings) extends BeanSerializer(typeMappings) {
  
  def javaBeanSupport = false
    
  def writeClass(model: EntityType, writer: ScalaWriter) = {
    model.getAnnotations foreach (writer.annotation(_))
    val parameters = model.getProperties.map(p => new Parameter(p.getEscapedName, p.getType)).toArray
    writer.caseClass(model.getSimpleName, parameters:_*) 
  }

}  
 
/**
 * @author tiwe
 *
 */
abstract class BeanSerializer(typeMappings: TypeMappings) extends Serializer {

  var createCompanionObject = true
  
  def javaBeanSupport: Boolean

  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val simpleName = model.getSimpleName

    // package
    if (!model.getPackageName.isEmpty) writer.packageDecl(model.getPackageName)

    // imports
    val importedClasses = getAnnotationTypes(model)
    if (javaBeanSupport)  importedClasses.add("scala.reflect.BeanProperty")    
    if (model.hasLists()) importedClasses.add(classOf[List[_]].getName)
    if (model.hasMaps())  importedClasses.add(classOf[Map[_, _]].getName)

    writer.importClasses(importedClasses.toArray: _*)
    
    if (createCompanionObject) {
      val queryType = typeMappings.getPathType(model, model, true)
      writeCompanionObject(model, queryType, writer.asInstanceOf[ScalaWriter])  
    } 
    
    writeClass(model, writer.asInstanceOf[ScalaWriter])
  }
  
  def writeClass(model: EntityType, writer: ScalaWriter)
  
  def writeCompanionObject(model: EntityType, queryType: Type, writer: ScalaWriter) = {
    val modelName = writer.getRawName(model)
    val queryTypeName = writer.getRawName(queryType)    
    val variable = model.getUncapSimpleName
    
    writer.beginObject(modelName + " extends "+queryTypeName+"(\""+variable+"\")")
    writer.line("override def as(variable: String) = new ", queryTypeName, "(variable)")
    writer.line("")
    writer.end()
  }  
  
  private def getAnnotationTypes(model: EntityType): Set[String] = {
    val imports = Set() ++ model.getAnnotations.map(_.annotationType.getName);
    // flatMap flattens the results of the map-function.
    // E.g. List(List(1,2,3), List(4,5,6)).flatMap(_.map(_*3)) ends up as List(3, 6, 9, 12, 15, 18).
    imports ++ model.getProperties.flatMap(_.getAnnotations.map(_.annotationType.getName));
  }
  
}


