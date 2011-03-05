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

class ScalaMetaDataSerializer @Inject() (typeMappings: TypeMappings, val namingStrategy: NamingStrategy) 
    extends ScalaEntitySerializer(typeMappings) {
    
  override val classHeaderFormat = "%1$s(cl: Class[_ <: %2$s], md: PathMetadata[_]) extends RelationalPathImpl[%2$s](cl, md)";

  override def writeHeader(model: EntityType, writer: ScalaWriter) {
    writer.imports(classOf[RelationalPathImpl[_]]);
    writer.imports(classOf[PrimaryKey[_]].getPackage);
    super.writeHeader(model, writer);            
  }
    
  override def writeAdditionalProperties(model: EntityType, writer: ScalaWriter) {
    // primary keys
    val primaryKeys: Collection[PrimaryKeyData] =
      model.getData.get(classOf[PrimaryKeyData]).asInstanceOf[Collection[PrimaryKeyData]];
    if (primaryKeys != null) {
      serializePrimaryKeys(model, writer, primaryKeys);
    }

    // foreign keys
    val foreignKeys: Collection[ForeignKeyData] =
      model.getData.get(classOf[ForeignKeyData]).asInstanceOf[Collection[ForeignKeyData]];
    if (foreignKeys != null) {
      serializeForeignKeys(model, writer, foreignKeys, false);
    }

    // inverse foreign keys
    val inverseForeignKeys: Collection[InverseForeignKeyData] =
      model.getData.get(classOf[InverseForeignKeyData]).asInstanceOf[Collection[InverseForeignKeyData]];
    if (inverseForeignKeys != null) {
      serializeForeignKeys(model, writer, inverseForeignKeys, true);
    }
  }

  def serializePrimaryKeys(model: EntityType, writer: CodeWriter, primaryKeys: Collection[PrimaryKeyData]) {
    primaryKeys foreach { primaryKey =>
      val fieldName = namingStrategy.getPropertyNameForPrimaryKey(primaryKey.getName(), model);
      val value = new StringBuilder("createPrimaryKey(");
      value.append(primaryKey.getColumns().map({ column =>
        namingStrategy.getPropertyName(column, model)
      }).mkString(", "));
      value.append(")");
      writer.publicFinal(new ClassType(classOf[PrimaryKey[_]], model), fieldName, value.toString);
    }
  }

  def serializeForeignKeys(model: EntityType, writer: CodeWriter, foreignKeys: Collection[_ <: KeyData], inverse: Boolean) {
    foreignKeys foreach { foreignKey =>
      var fieldName: String = null;
      if (inverse) {
        fieldName = namingStrategy.getPropertyNameForInverseForeignKey(foreignKey.getName, model);
      } else {
        fieldName = namingStrategy.getPropertyNameForForeignKey(foreignKey.getName, model);
      }
      val value = new StringBuilder();
      if (inverse) {
        value.append("createInvForeignKey(");
      } else {
        value.append("createForeignKey(");
      }
      if (foreignKey.getForeignColumns.size == 1) {
        value.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns.get(0), model));
        value.append(", \"" + foreignKey.getParentColumns().get(0) + "\"");
      } else {
        val local = new StringBuilder();
        val foreign = new StringBuilder();
        var i = 0;
        while (i < foreignKey.getForeignColumns().size()) {
          if (i > 0) {
            local.append(", ");
            foreign.append(", ");
          }
          local.append(namingStrategy.getPropertyName(foreignKey.getForeignColumns().get(0), model));
          foreign.append("\"" + foreignKey.getParentColumns.get(0) + "\"");
          i += 1;
        }
        value.append("Arrays.asList(" + local + "), Arrays.asList(" + foreign + ")");
      }
      value.append(")");
      val t = new ClassType(classOf[ForeignKey[_]], foreignKey.getType);
      writer.publicFinal(t, fieldName, value.toString());
    }
  }

}