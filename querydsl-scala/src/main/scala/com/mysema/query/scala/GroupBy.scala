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

package com.mysema.query.scala

import com.mysema.query.types._
import com.mysema.query.group._

object GroupBy extends GroupBy

/**
 * Group by result transforming functionality
 * 
 * @author tiwe
 *
 */
trait GroupBy {
  
  def groupBy[K](key: Ex[K]) = new GroupByBuilder[K](key)
  
  def set[T](e: Ex[T]) = new AbstractGroupExpression[T,Set[T]](classOf[Set[T]], e) {
    def createGroupCollector() : GroupCollector[T,Set[T]] = new GroupCollector[T, Set[T]]() {
      val set = Set.newBuilder[T]
      def add(o: T): Unit = set.+=(o) 
      def get(): Set[T] = set.result
    }  
  }
    
  def list[T](e: Ex[T]) = new AbstractGroupExpression[T,List[T]](classOf[List[T]], e) {
    def createGroupCollector(): GroupCollector[T,List[T]] = new GroupCollector[T, List[T]]() {
      val list = List.newBuilder[T]
      def add(o: T): Unit = list.+=(o) 
      def get(): List[T] = list.result
    }  
  }
      
  def map[K,V](k: Ex[K], v: Ex[V]) = new AbstractGroupExpression[(K,V),Map[K,V]](classOf[Map[K,V]], new Tu2Ex(k,v)) {
    def createGroupCollector(): GroupCollector[(K,V),Map[K,V]] = new GroupCollector[(K,V),Map[K,V]]() {
      val map = Map.newBuilder[K,V]
      def add(tu: (K,V)) = map.+=(tu)
      def get(): Map[K,V] = map.result
    }        
  }
    
}

abstract class AbstractGroupExpression[T, R](cl: Class[R], expr: Ex[T]) extends GroupExpression[T, R] {
    
  def getExpression(): Ex[T] = expr
  
  def getType: Class[R] = cl
  
  def accept[R,C](v: Visitor[R,C], context: C): R = expr.accept(v, context)
  
  override def equals(o: Any): Boolean = o match {
    case ge: GroupExpression[_,_] => ge.getExpression == expr
    case _ => false
  }
  
  override def hashCode(): Int = expr.hashCode()
    
}