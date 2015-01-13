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

import java.sql._
import javax.sql._
import com.querydsl.sql._
import com.querydsl.sql.dml._
import com.querydsl.core.types._

/**
 * Implicit conversion from RelationalPath to RichSimpleQuery
 */
trait SQLHelpers {
  
  def connection: Connection
  
  def templates: SQLTemplates

  implicit def toRichSimpleQuery[T, R <: RelationalPath[T]](p: RelationalPath[T] with R) = {
    new RichSimpleQuery[T, R, T, R](p, p, new SQLQuery(connection, templates).from(p) )
  }
  
}

/**
 * Helper trait with RelationalPath to RichSimpleQuery conversion, factory methods for
 * queries and DML clauses and transactional wrapping of code execution
 */
trait SQL extends SQLHelpers {
  
  private val connectionHolder = new ThreadLocal[Connection]
  
  val dataSource: DataSource
  
  def connection = connectionHolder.get()
               
  def query() = new SQLQuery(connection, templates)
  
  def from(expr: Expression[_]*) = query.from(expr:_*)

  def insert(path: RelationalPath[_]) = new SQLInsertClause(connection, templates, path)
  
  def update(path: RelationalPath[_]) = new SQLUpdateClause(connection, templates, path)
  
  def delete(path: RelationalPath[_]) = new SQLDeleteClause(connection, templates, path)
     
  def tx[R](fn: ⇒ R): R = {     
    val conn = dataSource.getConnection
    conn.setAutoCommit(false)
    connectionHolder.set(conn)
    try {
      val rv = fn
      conn.commit()
      rv
    } catch {
      case e: Exception ⇒ {
        conn.rollback()
        throw e
      }
    } finally {
      conn.close()
      connectionHolder.remove()
    }
  }
  
}