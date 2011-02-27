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

class ScalaEntitySerializer(val namePrefix: String, val nameSuffix: String) extends Serializer {
    
  val typeMappings = ScalaTypeMappings.typeMappings;

//  val classHeaderFormat = "%1$s(path: String) extends EntityPathImpl[%2$s](classOf[%2$s], path)";
  val classHeaderFormat = "%1$s(cl: Class[_ <: %2$s], md: PathMetadata[_]) extends EntityPathImpl[%2$s](cl, md)";
  
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter];
    val simpleName: String = model.getSimpleName;

    // package
    if (!model.getPackageName.isEmpty()) {
      writer.packageDecl(model.getPackageName);
    }

    // imports
    writer.importPackages("com.mysema.query.types","com.mysema.query.scala");
    writer.staticimports(classOf[PathMetadataFactory]);

    var importedClasses = getAnnotationTypes(model);
    if (model.hasLists()) {
      importedClasses.add(classOf[List[_]].getName);
    }
    if (model.hasMaps()) {
      importedClasses.add(classOf[Map[_, _]].getName);
    }

    writer.importClasses(importedClasses.toArray: _*);
    
    val queryType = typeMappings.getPathType(model, model, true);
    var modelName = writer.getRawName(model);
    var queryTypeName = writer.getRawName(queryType);
    var classHeader = String.format(classHeaderFormat, queryTypeName, modelName);
        
    scalaWriter.beginObject(queryTypeName);
    scalaWriter.line("def as(variable: String) = new ", queryTypeName, "(variable)");
    scalaWriter.end();
    
    // header
    model.getAnnotations.foreach(writer.annotation(_));
    
    scalaWriter.beginClass(classHeader);
    
    // entity property fields    
    model.getProperties filter (_.getType.getCategory == ENTITY) foreach { property =>
      var queryType = typeMappings.getPathType(property.getType, model, false);
      var typeName = writer.getRawName(queryType);
      scalaWriter.line("private var _", property.getEscapedName, ": ", typeName, " = _;\n");
    }
    
    // additional constructors
    scalaWriter.line("def this(variable: String) = this(classOf[",modelName,"], forVariable(variable));");
    scalaWriter.line("");
    scalaWriter.line("def this(parent: Path[_], variable: String) = this(classOf[",modelName,"], forProperty(parent, variable));");
    scalaWriter.line("");
    
    // entity properties
    model.getProperties filter (_.getType.getCategory == ENTITY) foreach { property =>
      var queryType = typeMappings.getPathType(property.getType, model, false);
      var typeName = writer.getRawName(queryType);
      var name = property.getEscapedName;
      val value = String.format("def %1$s: %2$s = { if (_%1$s == null){_%1$s = new %2$s(this, \"%1$s\"); }; _%1$s; }", name, typeName)
      scalaWriter.line(value, "\n");
    }
    
    // other properties
    serializeProperties(model, writer, model.getProperties  );

    writer.end();
  }

  def serializeProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) {
      
    
    properties filter (_.getType.getCategory != ENTITY) foreach { property =>
      val methodName: String = property.getType.getCategory match {
        case ARRAY => "createArray";
        case BOOLEAN => "createBoolean";  
        case COLLECTION => "createCollection";
        case COMPARABLE => "createComparable";        
        case DATE => "createDate";
        case DATETIME => "createDateTime";
        case ENUM => "createEnum";
        case LIST => "createList";
        case MAP => "createMap";
        case NUMERIC => "createNumber";
        case SET => "createSet";
        case SIMPLE => "createSimple";
        case STRING => "createString";
        case TIME => "createTime";     
      }
      
      var ptype: Type = null;            
      var value: String = null;
      val cat = property.getType.getCategory;
      if (cat == BOOLEAN || cat == STRING) {
        value = methodName + "(\"" + property.getName + "\")";
        ptype = typeMappings.getPathType(property.getType, model, false)
        
      } else if (cat == LIST || cat == SET || cat == COLLECTION) {
        val componentType = writer.getGenericName(true, property.getParameter(0));
        val queryType = typeMappings.getPathType(getRaw(property.getParameter(0)), model, false);
        value = methodName + "(\"" + property.getName + "\", classOf[" + componentType + "], classOf[" + writer.getGenericName(true, queryType) + "])";
        if (cat == LIST){
            ptype = new ClassType(classOf[ListPath[_,_]], property.getParameter(0), queryType);
        }else if (cat == SET){
            ptype = new ClassType(classOf[SetPath[_,_]], property.getParameter(0), queryType);
        }else{
            ptype = new ClassType(classOf[CollectionPath[_,_]], property.getParameter(0), queryType);
        }
          
      } else if (cat == MAP) {
        val keyType = writer.getGenericName(true, property.getParameter(0));
        val valueType = writer.getGenericName(true, property.getParameter(1));
        val queryType = typeMappings.getPathType(getRaw(property.getParameter(1)), model, false);
        value = methodName + "(\"" + property.getName + "\", classOf[" + keyType + "], classOf[" + valueType + "], classOf[" + writer.getGenericName(true, queryType) + "])";      
        ptype = new ClassType(classOf[MapPath[_,_,_]], property.getParameter(0), property.getParameter(1), queryType);
        
      } else {
        value = methodName + "(\"" + property.getName + "\", classOf[" + writer.getRawName(property.getType) + "])";
        ptype = typeMappings.getPathType(property.getType, model, false)
        if (cat == ARRAY){
            ptype = new ClassType(classOf[ArrayPath[_]], property.getType);
        }
      }
      
//      writer.publicFinal(ptype, property.getEscapedName, value);
      writer.line("val ", property.getEscapedName, " = ", value, ";\n");
    }
  }

  def getAnnotationTypes(model: EntityType): Set[String] = {
    val imports = Set();
    imports ++ (model.getAnnotations.map(_.annotationType.getName))
  }
  
  private def getRaw(t : Type): Type = {
    if (t.isInstanceOf[EntityType] && t.getPackageName.startsWith("ext.java")){
      t;
    }else{
      new SimpleType(t, t.getParameters);
    }
  }

}