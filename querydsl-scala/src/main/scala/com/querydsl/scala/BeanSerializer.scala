/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.querydsl.scala

import com.querydsl.codegen._
import com.mysema.codegen.{ CodeWriter, ScalaWriter }
import com.mysema.codegen.model.{ Parameter, Type }
import com.querydsl

import javax.inject.Inject

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._
import scala.collection.mutable.Set

import Serializer._

/**
 * Serializer contains common functionality used in the Serializer implementations
 */
object Serializer {
  
  /**
   * 
   */
  def writePackage(model: EntityType, writer: ScalaWriter) {
    if (!model.getPackageName.isEmpty) writer.packageDecl(model.getPackageName)
  }
  
  /**
   * 
   */
  def writeImports(model: EntityType, javaBeanSupport: Boolean, writer: ScalaWriter) {
    val importedClasses = getAnnotationTypes(model)
    if (javaBeanSupport)  importedClasses.add("scala.reflect.BeanProperty")    
    if (model.hasLists()) importedClasses.add(classOf[List[_]].getName)
    if (model.hasMaps())  importedClasses.add(classOf[Map[_, _]].getName)
    writer.importClasses(importedClasses.toArray: _*)    
  }
  
  /**
   * 
   */
  def writeCompanionObject(model: EntityType, typeMappings: TypeMappings, writer: ScalaWriter) {
    val queryType = typeMappings.getPathType(model, model, true)
    val modelName = writer.getRawName(model)
    val queryTypeName = writer.getRawName(queryType)    
    val variable = model.getUncapSimpleName
    
    writer.beginObject(modelName + " extends "+queryTypeName+"(\""+variable+"\")")
    writer.line("override def as(variable: String) = new ", queryTypeName, "(variable)")
    writer.line("")
    writer.end()
  }  
  
  private def getAnnotationTypes(model: EntityType): Set[String] = {
    val imports = Set() ++ model.getAnnotations.map(_.annotationType.getName)
    // flatMap flattens the results of the map-function.
    // E.g. List(List(1,2,3), List(4,5,6)).flatMap(_.map(_*3)) ends up as List(3, 6, 9, 12, 15, 18).
    imports ++ model.getProperties.flatMap(_.getAnnotations.map(_.annotationType.getName))
  }  
  
}

/**
 * Serializer implementation which serializes classes as mutable optionally JavaBean
 * compatible classes
 */
class ScalaBeanSerializer @Inject() (typeMappings: TypeMappings) extends Serializer {

  var javadocSuffix = " is a Querydsl bean type"
    
  var createCompanionObject = true  
  
  var javaBeanSupport = false
  
  /**
   * 
   */
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter]
    
    writePackage(model, scalaWriter)
    writeImports(model, javaBeanSupport, scalaWriter)
    if (createCompanionObject) {      
      writeCompanionObject(model, typeMappings, scalaWriter)  
    }
    writeClass(model, scalaWriter)
  }
    
  def writeClass(model: EntityType, writer: ScalaWriter) = {
    writer.javadoc(model.getSimpleName + javadocSuffix)
    model.getAnnotations foreach(writer.annotation(_))
    writer.beginClass(model)
    for (property <- model.getProperties) {
      property.getAnnotations.foreach(writer.annotation(_))
      if (javaBeanSupport) writer.line("@BeanProperty")
      writer.publicField(property.getType(), property.getEscapedName, "_")
    }
    writer.end()
  }
  
}

/**
 * Serializer implementation which serializes classes as Scala case classes
 */
class CaseClassSerializer @Inject() (typeMappings: TypeMappings) extends Serializer {
  
  var createCompanionObject = true
  
  /**
   * 
   */
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter]
    
    writePackage(model, scalaWriter)
    writeImports(model, false, scalaWriter)    
    if (createCompanionObject) {      
      writeCompanionObject(model, typeMappings, scalaWriter)  
    }     
    writeClass(model, scalaWriter)
  }
    
  def writeClass(model: EntityType, writer: ScalaWriter) = {
    model.getAnnotations foreach (writer.annotation(_))
    val parameters = model.getProperties
      .map(p => new Parameter(p.getEscapedName, p.getType)).toArray
    writer.caseClass(model.getSimpleName, parameters:_*) 
  }

}  
 



