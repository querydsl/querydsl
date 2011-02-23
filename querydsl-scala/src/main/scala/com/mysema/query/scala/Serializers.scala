package com.mysema.query.scala

import com.mysema.codegen.CodeWriter
import com.mysema.codegen.ScalaWriter
import com.mysema.codegen.model._
import com.mysema.codegen.model.TypeCategory._

import com.mysema.query
import com.mysema.query.codegen._
import com.mysema.query.sql._
import com.mysema.query.sql.support._

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._
import scala.collection.mutable.Set

class ScalaEntitySerializer(val namePrefix: String, val nameSuffix: String) extends Serializer {
    
  val typeMappings = new TypeMappings();

  val classHeaderFormat = "%1$s(path: String) extends EntityPathImpl[%2$s](classOf[%2$s], path)";
  
  def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
    val scalaWriter = writer.asInstanceOf[ScalaWriter];
    val simpleName: String = model.getSimpleName;

    // package
    if (!model.getPackageName.isEmpty()) {
      writer.packageDecl(model.getPackageName);
    }

    // imports
    writer.importPackages("com.mysema.query.types.path","com.mysema.query.scala");

    var importedClasses = getAnnotationTypes(model);
    if (model.hasLists()) {
      importedClasses.add(classOf[List[_]].getName);
    }
    if (model.hasMaps()) {
      importedClasses.add(classOf[Map[_, _]].getName);
    }

    writer.importClasses(importedClasses.toArray: _*);

    // javadoc        
    //writer.javadoc(simpleName + javadocSuffix);
    
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

    // properties
    serializeProperties(model, writer, model.getProperties);

    writer.end();
  }

  def serializeProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]) {
    properties foreach { property =>
      val methodName: String = property.getType.getCategory match {
        case COMPARABLE => "createComparable";
        case BOOLEAN => "createBoolean";
        case DATE => "createDate";
        case DATETIME => "createDateTime";
        case ENUM => "createEnum";
        case NUMERIC => "createNumber";
        case STRING => "createString";
        case SIMPLE => "createSimple";
        case TIME => "createTime";
        case LIST => "createList";
        case SET => "createSet";
        case COLLECTION => "createCollection";
        case MAP => "createMap";
      }
      var ptype = typeMappings.getPathType(property.getType, model, false);
      var value: String = null;
      val cat = property.getType.getCategory;
      if (cat == BOOLEAN || cat == STRING) {
        value = methodName + "(\"" + property.getName + "\")";
      } else if (cat == LIST || cat == SET || cat == COLLECTION) {
        // TODO   
      } else if (cat == MAP) {
        // TODO        
      } else {
        value = methodName + "(\"" + property.getName + "\", classOf[" + writer.getRawName(property.getType) + "])";
      }
      writer.publicFinal(ptype, property.getEscapedName, value);
    }
  }

  def getAnnotationTypes(model: EntityType): Set[String] = {
    val imports = Set();
    imports ++ (model.getAnnotations.map(_.annotationType.getName))
  }

}