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

import com.querydsl.codegen.TypeMappings
import com.mysema.codegen.model.TypeCategory
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Path

/**
 * Factory for TypeMappings creation
 * 
 * @author tiwe
 *
 */
object ScalaTypeMappings {
  
  def create: TypeMappings = new ScalaTypeMappings();
  
}

class ScalaTypeMappings extends TypeMappings {
  register(TypeCategory.STRING, classOf[StringExpression], classOf[StringPath], classOf[StringTemplate])
  register(TypeCategory.BOOLEAN, classOf[BooleanExpression], classOf[BooleanPath], classOf[BooleanTemplate])
  register(TypeCategory.COMPARABLE, classOf[ComparableExpression[_]], classOf[ComparablePath[_]], classOf[ComparableTemplate[_]])
  register(TypeCategory.ENUM, classOf[EnumExpression[_]], classOf[EnumPath[_]], classOf[EnumTemplate[_]])
  register(TypeCategory.DATE, classOf[DateExpression[_]], classOf[DatePath[_]], classOf[DateTemplate[_]])
  register(TypeCategory.DATETIME, classOf[DateTimeExpression[_]], classOf[DateTimePath[_]], classOf[DateTimeTemplate[_]])
  register(TypeCategory.TIME, classOf[TimeExpression[_]], classOf[TimePath[_]], classOf[TimeTemplate[_]])
  register(TypeCategory.NUMERIC, classOf[NumberExpression[_]], classOf[NumberPath[_]], classOf[NumberTemplate[_]])
  register(TypeCategory.SIMPLE, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
  register(TypeCategory.ARRAY, classOf[Expression[_]], classOf[ArrayPath[_]], classOf[SimpleTemplate[_]])
    
  register(TypeCategory.COLLECTION, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
  register(TypeCategory.SET, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
  register(TypeCategory.LIST, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
  register(TypeCategory.MAP, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])    
  register(TypeCategory.CUSTOM, classOf[Expression[_]], classOf[Path[_]], classOf[SimpleTemplate[_]])
  register(TypeCategory.ENTITY, classOf[Expression[_]], classOf[Path[_]], classOf[SimpleTemplate[_]]) 
}