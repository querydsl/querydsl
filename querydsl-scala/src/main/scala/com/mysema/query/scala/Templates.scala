package com.mysema.query.scala;

import com.mysema.query.types._;
import com.mysema.query.types.PathMetadataFactory._;
import com.mysema.query.codegen._;

import com.mysema.codegen.model.TypeCategory

// TODO : factory object for template creation 

class SimpleTemplate[T](t: Class[_ <: T], template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[T](t, template, args) with SimpleExpression[T] {

}

class ComparableTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[T](t, template, args) with ComparableExpression[T] {

}

class NumberTemplate[T <: Number with Comparable[T]](t: Class[_ <: T], template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[T](t, template, args) with NumberExpression[T] {

}

class BooleanTemplate(template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[java.lang.Boolean](classOf[java.lang.Boolean], template, args) with BooleanExpression {

}

class StringTemplate(template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[String](classOf[String], template, args) with StringExpression {

}

class DateTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[T](t, template, args) with DateExpression[T] {

}

class DateTimeTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template, args: java.util.List[Expression[_]])
  extends TemplateExpressionImpl[T](t, template, args) with DateTimeExpression[T] {

}

class TimeTemplate[T <: Comparable[_]](t: Class[_ <: T], template: Template)
  extends TemplateExpressionImpl[T](t, template) with TimeExpression[T] {

}

class EnumTemplate[T <: Enum[T]](t: Class[_ <: T], template: Template)
  extends TemplateExpressionImpl[T](t, template) with EnumExpression[T] {

}
