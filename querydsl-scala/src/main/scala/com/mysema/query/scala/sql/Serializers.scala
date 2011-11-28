package com.mysema.query.scala.sql

import com.mysema.query
import com.mysema.codegen.{ScalaWriter, CodeWriter}
import com.mysema.codegen.model._
import com.mysema.query.codegen._
import com.mysema.query.scala.ScalaEntitySerializer
import com.mysema.query.sql._
import com.mysema.query.sql.support._
import java.util._
import scala.collection.JavaConversions._

import javax.inject.Inject

/**
 * MetaDataSerializer implementation for Scala
 * 
 * @author tiwe
 *
 */
class ScalaMetaDataSerializer @Inject() (typeMappings: TypeMappings, val namingStrategy: NamingStrategy) 
    extends ScalaEntitySerializer(typeMappings) {
    
  override val classHeaderFormat = "%s(md: PathMetadata[_]) extends RelationalPathImpl[%s](md, %s, %s)"

  override def writeHeader(model: EntityType, writer: ScalaWriter) {
    writer.imports(classOf[RelationalPathImpl[_]])
    writer.imports(classOf[PrimaryKey[_]].getPackage)
    
    val queryType = typeMappings.getPathType(model, model, true)
    val modelName = writer.getRawName(model)
    val queryTypeName = writer.getRawName(queryType)
    val schema = model.getData.get("schema") match {
      case x: String => "\""+x+"\""
      case _ => "null"
    }
    val table = "\""+model.getData.get("table")+"\""
    val classHeader = String.format(classHeaderFormat, queryTypeName, modelName, schema, table)
    
    writeCompanionObject(model, queryType, writer)
    
    // header
    writer.beginClass(classHeader)
  }
    
  override def writeAdditionalConstructors(modelName: String, writer: ScalaWriter) = {
    writer.line("def this(variable: String) = this(forVariable(variable))\n")
    writer.line("def this(parent: Path[_], property: String) = this(forProperty(parent, property))\n")
  }
    
  override def writeAdditionalProperties(model: EntityType, writer: ScalaWriter) {
    // primary keys
    val primaryKeys: Collection[PrimaryKeyData] =
      model.getData.get(classOf[PrimaryKeyData]).asInstanceOf[Collection[PrimaryKeyData]]
    if (primaryKeys != null) serializePrimaryKeys(model, writer, primaryKeys)

    // foreign keys
    val foreignKeys: Collection[ForeignKeyData] =
      model.getData.get(classOf[ForeignKeyData]).asInstanceOf[Collection[ForeignKeyData]]
    if (foreignKeys != null) serializeForeignKeys(model, writer, foreignKeys, false)

    // inverse foreign keys
    val inverseForeignKeys: Collection[InverseForeignKeyData] =
      model.getData.get(classOf[InverseForeignKeyData]).asInstanceOf[Collection[InverseForeignKeyData]]
    if (inverseForeignKeys != null) serializeForeignKeys(model, writer, inverseForeignKeys, true)
  }
  
  override def writeAnnotations(model: EntityType, queryType: Type, writer: ScalaWriter) = {
    if (model == queryType) {
      model.getAnnotations.foreach(writer.annotation(_))  
    }    
  }
   
  def serializePrimaryKeys(model: EntityType, writer: CodeWriter, primaryKeys: Collection[PrimaryKeyData]) {
    primaryKeys.foreach { pk =>
      val fieldName = namingStrategy.getPropertyNameForPrimaryKey(pk.getName(), model)
      val value = pk.getColumns.map(c => escape(namingStrategy.getPropertyName(c, model)))
          .mkString("createPrimaryKey(", ", ", ")")
      writer.publicFinal(new ClassType(classOf[PrimaryKey[_]], model), fieldName, value)
    }
  }

  def serializeForeignKeys(model: EntityType, writer: CodeWriter, foreignKeys: Collection[_ <: KeyData], inverse: Boolean) {
    foreignKeys.foreach { fk =>
      val fieldName = if (inverse) {
        namingStrategy.getPropertyNameForInverseForeignKey(fk.getName, model)
      } else {
        namingStrategy.getPropertyNameForForeignKey(fk.getName, model)
      }
      val value = new StringBuilder(if (inverse) "createInvForeignKey(" else "createForeignKey(")
      if (fk.getForeignColumns.size == 1) {
        value.append(namingStrategy.getPropertyName(fk.getForeignColumns.get(0), model))
        value.append(", \"" + fk.getParentColumns().get(0) + "\"")
      } else {
        val local = fk.getForeignColumns.map(c => escape(namingStrategy.getPropertyName(c, model))).mkString(", ") 
        val foreign = fk.getParentColumns.map("\"" + _ + "\"").mkString(", ")        
        value.append("Arrays.asList(" + local + "), Arrays.asList(" + foreign + ")")
      }
      value.append(")")
      val t = new ClassType(classOf[ForeignKey[_]], fk.getType)
      writer.publicFinal(t, fieldName, value.toString())
    }
  }

}