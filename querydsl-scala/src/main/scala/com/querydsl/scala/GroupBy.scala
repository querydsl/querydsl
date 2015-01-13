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

import com.querydsl.core.types._
import com.querydsl.core.group._
import scala.collection.mutable.Builder
import GroupBy._

/**
 * Group by result transforming functionality
 * 
 * @author tiwe
 *
 */
object GroupBy extends GroupBy {
  
  class BuilderCollector[T,C](builder: Builder[T,C]) extends GroupCollector[T,C]{
    def add(o: T) { builder.+=(o) }
    def get(): C = builder.result()
  }
  
}

trait GroupBy {
    
  /**
   * 
   */
  def groupBy[K](key: Ex[K]) = new GroupByBuilder[K](key)
  
  /**
   * 
   */
  def set[T](e: Ex[T]) = new AbstractGroupExpression[T,Set[T]](classOf[Set[T]], e) {
    def createGroupCollector() = new BuilderCollector(Set.newBuilder[T])
  }
    
  /**
   * 
   */
  def list[T](e: Ex[T]) = new AbstractGroupExpression[T,List[T]](classOf[List[T]], e) {
    def createGroupCollector = new BuilderCollector(List.newBuilder[T])
  }
     
  /**
   * 
   */
  def map[K,V](k: Ex[K], v: Ex[V]) = new AbstractGroupExpression[(K,V),Map[K,V]](
      classOf[Map[K,V]], new Tu2Ex(k,v)) {
    def createGroupCollector = new BuilderCollector(Map.newBuilder[K,V])
  }
    
}