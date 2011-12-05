/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.scala;

import java.lang.reflect._
import javax.annotation.Nullable;
import scala.collection.mutable.ListBuffer

/**
 * @author tiwe
 *
 */
object ReflectionUtils {
    
  def getSuperClasses(cl: Class[_]): List[Class[_]] = { 
    if (cl == null) Nil else cl :: getSuperClasses(cl.getSuperclass)
  } 
    
  def getFields(cl: Class[_]): List[Field] = {
    getSuperClasses(cl).flatMap(_.getDeclaredFields)
  }
  
  def getImplementedInterfaces(cl: Class[_]): Set[Class[_]] = {
    getSuperClasses(cl).flatMap(_.getInterfaces).toSet
  }
  
  def getNameAndValue[T](o: AnyRef, field: Field): (String,T) = {
    field.setAccessible(true)
    val v = field.get(o).asInstanceOf[T]
    (field.getName, v)
  }  
  
}
