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

import com.querydsl.codegen.CodegenModule
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.GeneratedAnnotationResolver
import com.querydsl.codegen.ProjectionSerializer
import com.querydsl.codegen.SerializerConfig
import com.querydsl.codegen.TypeMappings
import com.querydsl.codegen.utils.CodeWriter
import com.querydsl.codegen.utils.model.Type
import com.querydsl.core.types.ConstructorExpression
import com.querydsl.core.types.Expression
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.joinToCode
import javax.annotation.Generated
import javax.inject.Inject
import javax.inject.Named

open class KotlinProjectionSerializer @Inject constructor(
    private val mappings: TypeMappings,
    @Named(CodegenModule.GENERATED_ANNOTATION_CLASS)
    private val generatedAnnotationClass: Class<out Annotation> = GeneratedAnnotationResolver.resolveDefault()
) : ProjectionSerializer {

    protected open fun intro(model: EntityType): TypeSpec.Builder {
        val queryType = mappings.getPathClassName(model, model)
        return TypeSpec.classBuilder(queryType)
            .superclass(ConstructorExpression::class.parameterizedBy(model))
            .addKdoc("${queryType.canonicalName} is a Querydsl Projection type for ${model.simpleName}")
            .addAnnotation(AnnotationSpec.builder(generatedAnnotationClass).addMember("%S", javaClass.name).build())
            .addType(introCompanion(model))
    }

    protected open fun introCompanion(model: EntityType): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addProperty(PropertySpec.builder("serialVersionUID", Long::class, KModifier.CONST, KModifier.PRIVATE).initializer("%L", model.hashCode()).build())
            .build()
    }

    protected open fun TypeSpec.Builder.outro(type: EntityType, writer: CodeWriter) {
        val queryType: Type = mappings.getPathType(type, type, false)
        FileSpec.builder(queryType.packageName, queryType.simpleName)
            .addType(build())
            .build()
            .writeTo(writer)
    }

    override fun serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
        val builder = intro(model)
        val sizes: MutableSet<Int> = mutableSetOf()
        for (c in model.constructors) {
            val asExpr = sizes.add(c.parameters.size)
            builder.addFunction(
                FunSpec.constructorBuilder()
                    .addParameters(c.parameters.map {
                        ParameterSpec.builder(
                            it.name, when {
                                !asExpr -> mappings.getExprType(it.type, model, false, false, true).asTypeName()
                                it.type.isFinal -> Expression::class.parameterizedBy(it.type.asTypeName())
                                else -> Expression::class.asClassName().parameterizedBy(WildcardTypeName.producerOf(it.type.asTypeName()))
                            }
                        ).build()
                    })
                    .callSuperConstructor(model.asClassNameStatement(),
                        c.parameters.map { it.type.asClassNameStatement() }.joinToCode(", ", "arrayOf(", ")"),
                        *c.parameters.map { CodeBlock.of("%L", it.name) }.toTypedArray()
                    )
                    .build()
            )
        }
        builder.outro(model, writer)
    }

}