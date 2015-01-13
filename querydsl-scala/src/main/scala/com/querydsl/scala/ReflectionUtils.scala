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

package com.querydsl.scala

import java.lang.reflect._

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
    (field.getName, field.get(o).asInstanceOf[T])
  }  
  
}
