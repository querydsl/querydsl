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

import com.google.common.collect.ImmutableList._
import com.mysema.commons.lang.Pair
import com.querydsl.scala._
import ReflectionUtils._
import com.querydsl.scala._
import com.querydsl.sql._
import com.querydsl.core.types._
import com.querydsl.core.types.PathMetadataFactory._
import java.util.{List => JavaList, ArrayList, Map => JavaMap, HashMap}
import java.lang.reflect._
import scala.reflect.BeanProperty

/**
 * Implementation of RelationalPath for Scala
 *
 * @author tiwe
 *
 */
class RelationalPathImpl[T](md: PathMetadata[_], schema: String, table: String)(implicit val mf: Manifest[T])
  extends BeanPath[T](mf.runtimeClass.asInstanceOf[Class[T]], md) with RelationalPath[T] {

  import scala.collection.JavaConversions._

  private var primaryKey: PrimaryKey[T] = _

  private lazy val schemaAndTable = new SchemaAndTable(schema, table)

  @BeanProperty
  val columns: JavaList[Path[_]] = new ArrayList[Path[_]]

  @BeanProperty
  val foreignKeys: JavaList[ForeignKey[_]] = new ArrayList[ForeignKey[_]]

  @BeanProperty
  val inverseForeignKeys: JavaList[ForeignKey[_]] = new ArrayList[ForeignKey[_]]

  @BeanProperty
  lazy val projection: FactoryExpression[T] = RelationalPathUtils.createProjection(this)

  private val metadata: JavaMap[Path[_], ColumnMetadata] = new HashMap[Path[_], ColumnMetadata]

  def this(variable: String, schema: String, table: String)(implicit mf: Manifest[T]) = {
    this(forVariable(variable), schema, table)(mf)
  }

  override def add[P <: Path[_]](p: P): P = {
    columns.add(p)
    p
  }

  protected def addMetadata(path: Path[_], md: ColumnMetadata) {
    metadata.put(path, md)
  }

  def createPrimaryKey(cols: Path[_]*): PrimaryKey[T] = {
    primaryKey = new PrimaryKey[T](this, cols:_*)
    primaryKey
  }

  def createForeignKey[F](local: Path[_], foreign: String) = {
    val foreignKey = new ForeignKey[F](this, local, foreign)
    foreignKeys.add(foreignKey)
    foreignKey
  }

  def createForeignKey[F](local: List[_ <: Path[_]], foreign: List[String]) = {
    val foreignKey = new ForeignKey[F](this, copyOf(local.iterator), copyOf(foreign.iterator))
    foreignKeys.add(foreignKey)
    foreignKey
  }

  def createInvForeignKey[F](local: Path[_], foreign: String) = {
    val foreignKey = new ForeignKey[F](this, local, foreign)
    inverseForeignKeys.add(foreignKey)
    foreignKey
  }

  def createInvForeignKey[F](local: List[_ <: Path[_]], foreign: List[String]) = {
    val foreignKey = new ForeignKey[F](this, copyOf(local.iterator), copyOf(foreign.iterator))
    inverseForeignKeys.add(foreignKey)
    foreignKey
  }

  def getMetadata(path: Path[_]): ColumnMetadata = metadata.get(path)

  def getPrimaryKey = primaryKey

  def getSchemaName = schema

  def getTableName = table

  def getSchemaAndTable = schemaAndTable

}