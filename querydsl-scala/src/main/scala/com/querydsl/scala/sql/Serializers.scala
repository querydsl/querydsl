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

package com.querydsl.scala.sql

import com.querydsl
import com.mysema.codegen.{ScalaWriter, CodeWriter}
import com.mysema.codegen.model._
import com.querydsl.codegen._
import com.querydsl.scala.ScalaEntitySerializer
import com.querydsl.sql._
import com.querydsl.sql.codegen._
import com.querydsl.sql.codegen.support._
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

    // metadata
    for (property <- model.getProperties) {
      val name = property.getEscapedName
      val metadata = property.getData.get("COLUMN").asInstanceOf[ColumnMetadata]
      val columnMeta = new StringBuilder()
      columnMeta.append("ColumnMetadata")
      columnMeta.append(".named(\"" + metadata.getName + "\")")
      columnMeta.append(".ofType(" + metadata.getJdbcType + ")")
      if (metadata.hasSize) {
        columnMeta.append(".withSize(" + metadata.getSize() + ")")
      }
      if (metadata.getDigits > 0) {
        columnMeta.append(".withDigits(" + metadata.getDigits() + ")")
      }
      if (!metadata.isNullable) {
        columnMeta.append(".notNull()")
      }
      writer.line("addMetadata(", escape(name), ", ", columnMeta.toString, ")")
    }
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

  def serializeForeignKeys(model: EntityType, writer: CodeWriter,
      foreignKeys: Collection[_ <: KeyData], inverse: Boolean) {
    for (fk <- foreignKeys) {
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
        value.append("List(" + local + "), List(" + foreign + ")")
      }
      value.append(")")
      val t = new ClassType(classOf[ForeignKey[_]], fk.getType)
      writer.publicFinal(t, fieldName, value.toString())
    }
  }

}