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

import com.mysema.codegen.CodeWriter
import com.mysema.codegen.ScalaWriter
import com.mysema.codegen.model._
import com.mysema.codegen.model.TypeCategory._
import com.mysema.codegen.support.ScalaSyntaxUtils
import com.querydsl
import com.querydsl.codegen._
import com.querydsl.sql._
import com.querydsl.sql.codegen.support._
import com.querydsl.core.types._
import java.util._
import java.io.IOException
import scala.reflect.BeanProperty
import scala.collection.JavaConversions._
import scala.collection.mutable.Set
import scala.collection.immutable.Map
import javax.inject.Inject

/**
 * EntitySerializer for Scala
 * 
 * @author tiwe
 *
 */
class ScalaEntitySerializer @Inject()(val typeMappings: TypeMappings) extends Serializer {
    
  private val methodNames = Map(ARRAY->"Array", BOOLEAN->"Boolean", COLLECTION->"Collection", 
      COMPARABLE->"Comparable",DATE->"Date", DATETIME->"DateTime", ENUM->"Enum", 
      LIST->"List", MAP->"Map", NUMERIC->"Number", SET->"Set", SIMPLE->"Simple", 
      STRING->"String", TIME->"Time")
  
  val classHeaderFormat = "%1$s(cl: Class[_ <: %2$s], md: PathMetadata[_]) extends EntityPathImpl[%2$s](cl, md)"
    
  var primitives = true  
    
  /**
   * 
   */
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter]
    val simpleName: String = model.getSimpleName

    // package
    if (!model.getPackageName.isEmpty()) writer.packageDecl(model.getPackageName)

    // imports
    writer.importPackages("com.querydsl.core.types","com.querydsl.scala")
    writer.staticimports(classOf[PathMetadataFactory])

    var importedClasses = getAnnotationTypes(model)
    if (model.hasLists()) importedClasses.add(classOf[java.util.List[_]].getName)
    if (model.hasMaps())  importedClasses.add(classOf[java.util.Map[_, _]].getName)
    writer.importClasses(importedClasses.toArray: _*)    
    
    writeHeader(model, scalaWriter)    
    
    var modelName = writer.getRawName(model)    
    writeAdditionalFields(model, scalaWriter)
    writeAdditionalConstructors(modelName, scalaWriter)

    // properties
    serializeProperties(model, writer, model.getProperties  )

    writeAdditionalProperties(model, scalaWriter)
    
    writer.end()
  }
  
  def writeAdditionalConstructors(modelName: String, writer: ScalaWriter) = {
    writer.line("def this(variable: String) = this(classOf[",
        modelName,"], forVariable(variable))\n")
    writer.line("def this(parent: Path[_], variable: String) = this(classOf[",
        modelName,"], forProperty(parent, variable))\n")
  }
  
  def writeAdditionalFields(model: EntityType, writer: ScalaWriter) = {
      // override to customize
  }
  
  def writeAdditionalProperties(model: EntityType, writer: ScalaWriter) = {
      // override to customize
  }
  
  def writeHeader(model: EntityType, writer: ScalaWriter) {
    val queryType = typeMappings.getPathType(model, model, true)
    val modelName = writer.getRawName(model)
    val queryTypeName = writer.getRawName(queryType)
    val classHeader = String.format(classHeaderFormat, queryTypeName, modelName)
    
    writeCompanionObject(model, queryType, writer)
    
    // header
    writeAnnotations(model, queryType, writer)
    writer.beginClass(classHeader)
  }
  
  def writeCompanionObject(model: EntityType, queryType: Type, writer: ScalaWriter) = {
    val queryTypeName = writer.getRawName(queryType)    
    val variable = model.getUncapSimpleName
    
    writer.beginObject(queryTypeName + " extends "+queryTypeName+"(\""+variable+"\")")
    writer.line("override def as(variable: String) = new ", queryTypeName, "(variable)")
    writer.line("")
    writer.end()
  }
  
  def writeAnnotations(model: EntityType, queryType: Type, writer: ScalaWriter) = {
    model.getAnnotations.foreach(writer.annotation(_))
  }
  
  def writeAdditionalCompanionContent(model: EntityType, writer: ScalaWriter) = {}

  private def getEntityProperties(model: EntityType, writer: CodeWriter, 
      properties: Collection[Property]) = {
    for (property <- properties if property.getType.getCategory == ENTITY) yield {
      val queryType = typeMappings.getPathType(property.getType, model, false)
      val typeName = writer.getRawName(queryType)
      val name = escape(property.getEscapedName)
      String.format("lazy val %1$s = new %2$s(this, \"%1$s\")", name, typeName)
    }
  }
  
  private def getOtherProperties(model: EntityType, writer: CodeWriter, 
      properties: Collection[Property]) = {
    for (property <- properties if property.getType.getCategory != ENTITY) yield { 
      val name = normalizeProperty(property.getName)
      val methodName: String = "create" + methodNames(property.getType.getCategory)
      
      val value = property.getType.getCategory match {
        case BOOLEAN | STRING => methodName + "(\"" + name + "\")"
        case LIST | SET | COLLECTION => {
          val componentType = writer.getGenericName(true, property.getParameter(0))
          val queryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false)
          methodName + "["+componentType+","+
            writer.getGenericName(true, queryType)+"](\"" + name+ "\")"
        }
        case MAP => {
          val keyType = writer.getGenericName(true, property.getParameter(0))
          val valueType = writer.getGenericName(true, property.getParameter(1))
          val queryType = typeMappings.getPathType(getRaw(property.getParameter(1)), model, false)
          methodName + "["+keyType+","+valueType+","+
            writer.getGenericName(true, queryType)+"](\"" + name + "\")"      
        }
        case _ => methodName + "[" + getName(property.getType, writer) + "](\"" + name + "\")"
      }
      (normalizeProperty(property.getEscapedName), value)
    }
  }
  
  private def normalizeProperty(name: String) = {
    if (name.contains("$")) name.substring(name.lastIndexOf("$")+1) else name
  }
  
  private def getName(t: Type, writer: CodeWriter) = {
    if (t == Types.CHARACTER) {
      "Character" // because createComparable[Char] doesn't work, and the workaround involves implicit params
    } else if (primitives && Types.PRIMITIVES.containsKey(t)) {
      writer.getRawName(Types.PRIMITIVES.get(t)) 
    } else {
      writer.getGenericName(true, t)
    }
  }
  
  def serializeProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) {
    // entity properties
    getEntityProperties(model, writer, properties) foreach (writer.line(_, "\n"))
    
    // other properties
    getOtherProperties(model, writer, properties) foreach { case (propertyName, value) => 
      writer.line("val ", escape(propertyName), " = ", value, "\n")
    }
  }
  
  def escape(token: String): String = {
    if (ScalaSyntaxUtils.isReserved(token)) "`" + token + "`" 
    else if (token == "count") token + "_" 
    else token 
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