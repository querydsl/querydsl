package com.mysema.query.scala.sql

import java.sql._
import javax.sql._
import com.mysema.query.sql._
import com.mysema.query.sql.dml._
import com.mysema.query.types._

/**
 * Convenience trait for injection into service implementations
 * 
 * @author tiwe
 *
 */
trait SQL {
  
  private val connectionHolder = new ThreadLocal[Connection]
  
  val dataSource: DataSource
  
  val templates : SQLTemplates

  private def connection = connectionHolder.get()
  
  implicit def toRichSimpleQuery[T, R <: RelationalPath[T]](p: RelationalPath[T] with R) = {
    new RichSimpleQuery[T, R](p, new SQLQueryImpl(connection, templates).from(p) )
  }
             
  def query() = new SQLQueryImpl(connection, templates)
  
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