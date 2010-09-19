package com.mysema.query.scala.sql

import com.mysema.query.codegen._
import com.mysema.codegen.CodeWriter
import com.mysema.codegen.model.TypeCategory._
import com.mysema.query

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

class ScalaMetaDataSerializer extends Serializer {
    
    val typeMappings = new TypeMappings();
    
    val javadocSuffix = " is a Querydsl query type";
    
    @throws(classOf[IOException])
    def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
        val simpleName: String = model.getSimpleName;
        
        // package
        if (!model.getPackageName.isEmpty()){
            writer.packageDecl(model.getPackageName);
        }
        
        // imports
        writer.importPackages("com.mysema.query.sql");
        writer.importPackages("com.mysema.query.types.path");
        
        var importedClasses: Set[String] = getAnnotationTypes(model);
        if (model.hasLists()){
            importedClasses.add(classOf[List[_]].getName);
        }
        if (model.hasMaps()){
            importedClasses.add(classOf[Map[_,_]].getName);
        }
        
        importedClasses.foreach( { writer.importClasses(_)} )
               
        // javadoc        
        writer.javadoc(simpleName + javadocSuffix);
        
        // header
        for (annotation <- model.getAnnotations){
            writer.annotation(annotation);
        }               

        val queryType = typeMappings.getPathType(model, model, true);
        writer.beginClass(queryType);
        
        // properties
        for (property <- model.getProperties()){
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
            }
            var ptype = typeMappings.getPathType(property.getType, model, false);
            var value: String = null;
            if (property.getType.getCategory == BOOLEAN || property.getType.getCategory == STRING){
                value = methodName + "(\"" + property.getName + "\")";  
            }else{
                value = methodName + "(\"" + property.getName + "\", classOf[" + writer.getRawName(property.getType) + "])";                
            }
            writer.publicFinal(ptype, property.getEscapedName, value);
        }
        
        // TODO : primary keys
        
        // TODO : foreign keys
        
        // TODO : inverse foreign keys
                
        writer.end();
    }

    def getAnnotationTypes(model: EntityType): Set[String] = {
        var imports = new HashSet[String]();
        for (annotation <- model.getAnnotations){
            imports.add(annotation.annotationType.getName);
        }
        imports;
    }    
    
}