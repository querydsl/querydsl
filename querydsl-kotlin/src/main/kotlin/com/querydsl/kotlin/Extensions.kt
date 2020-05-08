package com.querydsl.kotlin

import com.mysema.codegen.model.Type
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.TypeMappings
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.joinToCode
import kotlin.reflect.KClass

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

fun Type.asOutTypeName() = WildcardTypeName.producerOf(asTypeName())

private fun Type.enclosingTypeHierarchy(): List<String> {
    var current: Type? = this
    return generateSequence { current?.also { current = it.enclosingType }?.simpleName }.toList().asReversed()
}

fun ClassName.asClassStatement() = CodeBlock.of("%T::class.java", this)

fun Type.asClassNameStatement() = asClassName().asClassStatement()

fun TypeMappings.getPathClassName(type: Type, model: EntityType) = getPathType(type, model, true).asClassName()

fun TypeMappings.getPathTypeName(type: Type, model: EntityType) = getPathType(type, model, false).asTypeName()

fun KClass<*>.parameterizedBy(vararg types: TypeName) = asTypeName().parameterizedBy(*types)

fun KClass<*>.parameterizedBy(vararg types: Type) = asTypeName().parameterizedBy(types.map { it.asTypeName() })

fun Collection<String>.joinToCode(
        format: String = "%S",
        separator: CharSequence = ", ",
        prefix: CharSequence = "",
        suffix: CharSequence = "") = map { it.asCodeBlock(format) }.joinToCode(separator, prefix, suffix)

fun Any.asCodeBlock(format: String = "%L") = CodeBlock.of(format, this)

fun ParameterSpec.asCodeBlock(format: String = "%N") = (this as Any).asCodeBlock(format)
