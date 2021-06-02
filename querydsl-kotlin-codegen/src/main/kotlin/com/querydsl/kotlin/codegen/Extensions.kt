/*
 * Copyright 2021, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.kotlin.codegen

import com.querydsl.codegen.EntityType
import com.querydsl.codegen.TypeMappings
import com.querydsl.codegen.utils.model.Type
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
    "java.lang.Boolean", "boolean" -> Boolean::class.asClassName()
    "java.lang.Byte", "byte" -> Byte::class.asClassName()
    "java.lang.Character", "char" -> Char::class.asClassName()
    "java.lang.Short", "short" -> Short::class.asClassName()
    "java.lang.Integer", "int" -> Int::class.asClassName()
    "java.lang.Long", "long" -> Long::class.asClassName()
    "java.lang.Float", "float" -> Float::class.asClassName()
    "java.lang.Double", "double" -> Double::class.asClassName()
    "boolean[]" -> BooleanArray::class.asClassName()
    "byte[]" -> ByteArray::class.asClassName()
    "char[]" -> CharArray::class.asClassName()
    "short[]" -> ShortArray::class.asClassName()
    "int[]" -> IntArray::class.asClassName()
    "long[]" -> LongArray::class.asClassName()
    "float[]" -> FloatArray::class.asClassName()
    "double[]" -> DoubleArray::class.asClassName()
    "java.lang.String" -> String::class.asClassName()
    "java.util.List" -> List::class.asClassName()
    "java.util.Map" -> Map::class.asClassName()
    "java.util.Set" -> Set::class.asClassName()
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
