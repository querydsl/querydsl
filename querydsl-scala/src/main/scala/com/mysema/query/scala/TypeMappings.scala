package com.mysema.query.scala

import com.mysema.query.codegen.TypeMappings
import com.mysema.codegen.model.TypeCategory
import com.mysema.query.types.Expression
import com.mysema.query.types.Path

/**
 * Factory for TypeMappings creation
 * 
 * @author tiwe
 *
 */
object ScalaTypeMappings {
  
  def create: TypeMappings = {
    val typeMappings = new TypeMappings()
    typeMappings.register(TypeCategory.STRING, classOf[StringExpression], classOf[StringPath], classOf[StringTemplate])
    typeMappings.register(TypeCategory.BOOLEAN, classOf[BooleanExpression], classOf[BooleanPath], classOf[BooleanTemplate])
    typeMappings.register(TypeCategory.COMPARABLE, classOf[ComparableExpression[_]], classOf[ComparablePath[_]], classOf[ComparableTemplate[_]])
    typeMappings.register(TypeCategory.ENUM, classOf[EnumExpression[_]], classOf[EnumPath[_]], classOf[EnumTemplate[_]])
    typeMappings.register(TypeCategory.DATE, classOf[DateExpression[_]], classOf[DatePath[_]], classOf[DateTemplate[_]])
    typeMappings.register(TypeCategory.DATETIME, classOf[DateTimeExpression[_]], classOf[DateTimePath[_]], classOf[DateTimeTemplate[_]])
    typeMappings.register(TypeCategory.TIME, classOf[TimeExpression[_]], classOf[TimePath[_]], classOf[TimeTemplate[_]])
    typeMappings.register(TypeCategory.NUMERIC, classOf[NumberExpression[_]], classOf[NumberPath[_]], classOf[NumberTemplate[_]])
    typeMappings.register(TypeCategory.SIMPLE, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
    typeMappings.register(TypeCategory.ARRAY, classOf[Expression[_]], classOf[ArrayPath[_]], classOf[SimpleTemplate[_]])
    
    typeMappings.register(TypeCategory.COLLECTION, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
    typeMappings.register(TypeCategory.SET, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
    typeMappings.register(TypeCategory.LIST, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])
    typeMappings.register(TypeCategory.MAP, classOf[Expression[_]], classOf[SimplePath[_]], classOf[SimpleTemplate[_]])    
    typeMappings.register(TypeCategory.CUSTOM, classOf[Expression[_]], classOf[Path[_]], classOf[SimpleTemplate[_]])
    typeMappings.register(TypeCategory.ENTITY, classOf[Expression[_]], classOf[Path[_]], classOf[SimpleTemplate[_]])
    typeMappings
  }
  
}