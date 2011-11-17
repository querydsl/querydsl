package com.mysema.query.scala.sql

import com.mysema.query.sql._

/**
 * To be used with RelationalPath companion objects for access to the default path
 * 
 * @author tiwe
 */
trait Relation[T] {
  
  def path: RelationalPath[T]
  
}

