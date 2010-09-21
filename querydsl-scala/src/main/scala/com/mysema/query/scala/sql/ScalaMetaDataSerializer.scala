package com.mysema.query.scala.sql

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

class ScalaMetaDataSerializer(val namePrefix: String, val namingStrategy: NamingStrategy) extends Serializer {
    
    val typeMappings = new TypeMappings();
    
    val classHeaderFormat = "%1$s(path: String) extends RelationalPathBase[%2$s](classOf[%2$s], path)";
    
    val javadocSuffix = " is a Querydsl query type";
    
    def serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
        val simpleName: String = model.getSimpleName;
        
        // package
        if (!model.getPackageName.isEmpty()){
            writer.packageDecl(model.getPackageName);
        }
        
        // imports
        writer.importPackages("com.mysema.query.sql", "com.mysema.query.types.path");
        
        var importedClasses: Set[String] = getAnnotationTypes(model);
        importedClasses.add("java.util.Arrays");
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
        //QUser(path: String) extends RelationalPathBase[QUser](classOf[QUser], path)
        var modelName = writer.getRawName(model);
        var queryTypeName = writer.getRawName(queryType);
        var classHeader = String.format(classHeaderFormat, queryTypeName, modelName);
        writer.asInstanceOf[ScalaWriter].beginClass(classHeader);
        
        // properties
        serializeProperties(model, writer, model.getProperties);
        
        // primary keys
        val primaryKeys: Collection[PrimaryKeyData] = 
            model.getData.get(classOf[PrimaryKeyData]).asInstanceOf[Collection[PrimaryKeyData]];
        if (primaryKeys != null){
            serializePrimaryKeys(model, writer, primaryKeys);
        }

        // foreign keys
        val foreignKeys: Collection[ForeignKeyData] = 
            model.getData.get(classOf[ForeignKeyData]).asInstanceOf[Collection[ForeignKeyData]];
        if (foreignKeys != null){
            serializeForeignKeys(model, writer, foreignKeys, false);
        }
        
        // inverse foreign keys
        val inverseForeignKeys: Collection[InverseForeignKeyData] = 
            model.getData.get(classOf[InverseForeignKeyData]).asInstanceOf[Collection[InverseForeignKeyData]];
        if (inverseForeignKeys != null){
            serializeForeignKeys(model, writer, inverseForeignKeys, true);
        }
                
        writer.end();
    }
    
    def serializeProperties(model: EntityType, writer: CodeWriter, properties: Collection[Property]){
        for (property <- properties){
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
    }
    
    def serializePrimaryKeys(model: EntityType, writer: CodeWriter, primaryKeys: Collection[PrimaryKeyData]) {
        for (primaryKey <- primaryKeys){
            val fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
            val value: StringBuilder = new StringBuilder("createPrimaryKey(");
            var first = true;
            for (column <- primaryKey.getColumns()){
                if (!first){
                    value.append(", ");
                }
                value.append(namingStrategy.getPropertyName(column, namePrefix, model));
                first = false;
            }
            value.append(")");
            writer.publicFinal(new ClassType(classOf[PrimaryKey[_]], model), fieldName, value.toString());
        }
    }

    def serializeForeignKeys(model: EntityType, writer: CodeWriter, foreignKeys: Collection[_ <: KeyData] , inverse: Boolean){
        for (foreignKey <- foreignKeys){
            var fieldName: String = null;
            if (inverse){
                fieldName = namingStrategy.getPropertyNameForInverseForeignKey(foreignKey.getName, model);
            }else{
                fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName, model);
            }
            var foreignType: String = namingStrategy.getClassName(namePrefix, foreignKey.getTable);
            if (!model.getPrefix.isEmpty){
                foreignType = foreignType.substring(namePrefix.length);
            }
            val value = new StringBuilder();
            if (inverse){
                value.append("createInvForeignKey(");
            }else{
                value.append("createForeignKey(");    
            }            
            if (foreignKey.getForeignColumns.size == 1){
                value.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns.get(0), namePrefix, model));
                value.append(", \"" + foreignKey.getParentColumns().get(0) + "\"");
            }else{
                val local = new StringBuilder();
                val foreign = new StringBuilder();
                var i = 0;
                while (i < foreignKey.getForeignColumns().size()){
                    if (i > 0){
                        local.append(", ");
                        foreign.append(", ");
                    }
                    local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), namePrefix, model));
                    foreign.append("\"" +foreignKey.getParentColumns.get(0) + "\"");
                    i += 1;
                }
                value.append("Arrays.asList("+local+"), Arrays.asList("+foreign+")");
            }
            value.append(")");
            val t = new ClassType(
                    classOf[ForeignKey[_]], 
                    new SimpleType(model.getPackageName+"."+foreignType, model.getPackageName, foreignType));
            writer.publicFinal(t, fieldName, value.toString());
        }
    }

    def getAnnotationTypes(model: EntityType): Set[String] = {
        var imports = new HashSet[String]();
        for (annotation <- model.getAnnotations){
            imports.add(annotation.annotationType.getName);
        }
        imports;
    }    
    
}