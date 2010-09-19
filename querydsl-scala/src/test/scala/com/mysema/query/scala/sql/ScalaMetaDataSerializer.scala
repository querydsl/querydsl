package com.mysema.query.scala.sql

import com.mysema.query.codegen.{Serializer, EntityType, SerializerConfig}
import com.mysema.codegen.CodeWriter
import com.mysema.query

import java.util._
import java.io.IOException

import scala.reflect.BeanProperty
import scala.collection.JavaConversions._

class ScalaMetaDataSerializer extends Serializer {
    
    val javadocSuffix = " is a Querydsl query type";
    
    @throws(classOf[IOException])
    def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
        val simpleName: String = model.getSimpleName;
        
        // package
        if (!model.getPackageName.isEmpty()){
            writer.packageDecl(model.getPackageName);
        }
        
        // imports
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
        writer.beginClass(model);
        
        // properties
        for (property <- model.getProperties()){
            writer.publicField(property.getType(), property.getEscapedName);
        }
        
        // TODO : primary key
        
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