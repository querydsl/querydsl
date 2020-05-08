package com.querydsl.kotlin

import com.google.common.collect.Sets
import com.mysema.codegen.CodeWriter
import com.mysema.codegen.model.ClassType
import com.mysema.codegen.model.Type
import com.mysema.codegen.model.TypeCategory
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.ProjectionSerializer
import com.querydsl.codegen.SerializerConfig
import com.querydsl.codegen.TypeMappings
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

class KotlinProjectionSerializer @Inject constructor(private val mappings: TypeMappings) : ProjectionSerializer {

    protected fun intro(model: EntityType): TypeSpec.Builder {
        val simpleName = model.simpleName
        val queryType = mappings.getPathType(model, model, false)

        // class header
        val superType: Type = ClassType(TypeCategory.SIMPLE, ConstructorExpression::class.java, model)
        return TypeSpec.classBuilder(queryType.asClassName())
                .superclass(superType.asTypeName())
                .addKdoc("$queryType is a Querydsl Projection type for $simpleName")
                .addAnnotation(AnnotationSpec.builder(Generated::class).addMember("%S", javaClass.name).build())
                .addType(introCompanion(model))
    }

    protected fun introCompanion(model: EntityType): TypeSpec {
        return TypeSpec.companionObjectBuilder()
                .addProperty(PropertySpec.builder("serialVersionUID", Long::class, KModifier.CONST, KModifier.PRIVATE).initializer("%L", model.hashCode()).build())
                .build()
    }

    protected fun TypeSpec.Builder.outro(type: EntityType, writer: CodeWriter) {
        val queryType: Type = mappings.getPathType(type, type, false)
        FileSpec.builder(queryType.packageName, queryType.simpleName)
                .addType(build())
                .build()
                .writeTo(writer)
    }

    override fun serialize(model: EntityType, serializerConfig: SerializerConfig, writer: CodeWriter) {
        // intro
        val builder = intro(model)
        val localName = model
        val sizes: MutableSet<Int> = Sets.newHashSet()
        for (c in model.constructors) {
            val asExpr = sizes.add(c.parameters.size)
            builder.addFunction(FunSpec.constructorBuilder()
                    .addParameters(c.parameters.map {
                        ParameterSpec.builder(it.name, when {
                            !asExpr -> mappings.getExprType(it.type, model, false, false, true).asTypeName()
                            it.type.isFinal -> Expression::class.java.asClassName().parameterizedBy(it.type.asTypeName())
                            else -> Expression::class.java.asClassName().parameterizedBy(WildcardTypeName.producerOf(it.type.asTypeName()))
                        }).build()
                    })
                    .callSuperConstructor(localName.asClassName().asClassStatement(),
                            c.parameters.map { it.type.asClassName().asClassStatement() }.joinToCode(", ", "arrayOf(", ")"),
                            *c.parameters.map { CodeBlock.of("%L", it.name) }.toTypedArray())
                    .build())
        }

        // outro
        builder.outro(model, writer)
    }

}