package com.querydsl.kotlin

import com.mysema.codegen.model.Type
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

fun Type.asTypeName(): TypeName = asClassName().let { className ->
    if (parameters.isNotEmpty())
        className.parameterizedBy(*parameters.map { it.asTypeName() }.toTypedArray()) else className
}

fun Type.asClassName(): ClassName = when (this.fullName) {
    "java.lang.Boolean" -> Boolean::class.asClassName()
    "java.lang.Byte" -> Byte::class.asClassName()
    "java.lang.Character" -> Char::class.asClassName()
    "java.lang.Short" -> Short::class.asClassName()
    "java.lang.Integer" -> Int::class.asClassName()
    "java.lang.Long" -> Long::class.asClassName()
    "java.lang.Float" -> Float::class.asClassName()
    "java.lang.Double" -> Double::class.asClassName()
    else -> ClassName(packageName, *enclosingTypeHierarchy().toTypedArray())
}

private fun Type.enclosingTypeHierarchy() : List<String> {
    var current : Type? = this
    return generateSequence { current?.also { current = it.enclosingType }?.simpleName }.toList().asReversed()
}

fun ClassName.asClassStatement() = CodeBlock.of("%T::class.java", this)
