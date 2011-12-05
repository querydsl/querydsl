/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.types.PathMetadataFactory._;
import com.mysema.query.codegen._;
import TypeDefs._

import com.mysema.codegen.model.TypeCategory

/**
 * Factory for templates
 * 
 * @author tiwe
 *
 */
object Templates {

  def simple[T](t: Class[_ <: T], tpl: Template, args: Ex[_]*): SimpleExpression[T] = new SimpleTemplate[T](t, tpl, args: _*)

  def comparable[T <: Comparable[_]](t: Class[_ <: T], tpl: Template, args: Ex[_]*): ComparableExpression[T] = new ComparableTemplate[T](t, tpl, args: _*)

  def date[T <: Comparable[_]](t: Class[_ <: T], tpl: Template, args: Ex[_]*): DateExpression[T] = new DateTemplate[T](t, tpl, args: _*)

  def dateTime[T <: Comparable[_]](t: Class[_ <: T], tpl: Template, args: Ex[_]*): DateTimeExpression[T] = new DateTimeTemplate[T](t, tpl, args: _*)

  def time[T <: Comparable[_]](t: Class[_ <: T], tpl: Template, args: Ex[_]*): TimeExpression[T] = new TimeTemplate[T](t, tpl, args: _*)

  def number[T <: Number with Comparable[T]](t: Class[_ <: T], tpl: Template, args: Ex[_]*): NumberExpression[T] = new NumberTemplate[T](t, tpl, args: _*)

  def boolean(tpl: Template, args: Ex[_]*): BooleanExpression = new BooleanTemplate(tpl, args: _*)

  def string(tpl: Template, args: Ex[_]*): StringExpression = new StringTemplate(tpl, args: _*)

  def enum[T <: Enum[T]](t: Class[T], tpl: Template, args: Ex[_]*): EnumExpression[T] = new EnumTemplate[T](t, tpl, args: _*)

}

class SimpleTemplate[T](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with SimpleExpression[T]


class ComparableTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with ComparableExpression[T]


class NumberTemplate[T <: Number with Comparable[T]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with NumberExpression[T]


class BooleanTemplate(template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[java.lang.Boolean](classOf[java.lang.Boolean], template, args:_*) with BooleanExpression

  
class StringTemplate(template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[String](classOf[String], template, args:_*) with StringExpression
  
  
class DateTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with DateExpression[T]


class DateTimeTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with DateTimeExpression[T]


class TimeTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with TimeExpression[T]


class EnumTemplate[T <: Enum[T]](t: Class[_ <: T], template: Template, args: Ex[_]*)
  extends TemplateExpressionImpl[T](t, template, args:_*) with EnumExpression[T]
