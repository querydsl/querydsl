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
import com.querydsl.codegen.EntitySerializer
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.GeneratedAnnotationResolver
import com.querydsl.codegen.Property
import com.querydsl.codegen.SerializerConfig
import com.querydsl.codegen.TypeMappings
import com.querydsl.codegen.utils.CodeWriter
import com.querydsl.codegen.utils.model.SimpleType
import com.querydsl.codegen.utils.model.Type
import com.querydsl.codegen.utils.model.TypeCategory
import com.querydsl.core.types.Path
import com.querydsl.core.types.PathMetadata
import com.querydsl.core.types.PathMetadataFactory
import com.querydsl.core.types.dsl.ArrayPath
import com.querydsl.core.types.dsl.BooleanPath
import com.querydsl.core.types.dsl.CollectionPath
import com.querydsl.core.types.dsl.CollectionPathBase
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.ListPath
import com.querydsl.core.types.dsl.MapPath
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.SetPath
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.core.types.dsl.TimePath
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

open class KotlinEntitySerializer @Inject constructor(
    private val mappings: TypeMappings,
    @Named(CodegenModule.KEYWORDS)
    protected val keywords: Collection<String>,
    @Named(CodegenModule.GENERATED_ANNOTATION_CLASS)
    private val generatedAnnotationClass: Class<out Annotation> = GeneratedAnnotationResolver.resolveDefault()
) : EntitySerializer {

    override fun serialize(model: EntityType, config: SerializerConfig, writer: CodeWriter) {
        val queryType: Type = mappings.getPathType(model, model, false)
        FileSpec.builder(queryType.packageName, queryType.simpleName)
            .addImport(PathMetadataFactory::class, "forVariable", "forProperty")
            .addType(
                intro(model, config)
                    .serializeProperties(model, config)
                    .constructors(model, config)
                    .build()
            )
            .build()
            .writeTo(writer)
    }

    protected open fun intro(model: EntityType, config: SerializerConfig): TypeSpec.Builder {
        return introClassHeader(model, config)
            .introJavadoc(model, config)
            .introSuper(model)
    }

    protected open fun introClassHeader(model: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val pathType = if (model.properties.isEmpty()) {
            when (model.originalCategory) {
                TypeCategory.COMPARABLE -> ComparablePath::class
                TypeCategory.ENUM -> EnumPath::class
                TypeCategory.DATE -> DatePath::class
                TypeCategory.DATETIME -> DateTimePath::class
                TypeCategory.TIME -> TimePath::class
                TypeCategory.NUMERIC -> NumberPath::class
                TypeCategory.STRING -> StringPath::class
                TypeCategory.BOOLEAN -> BooleanPath::class
                else -> defaultSuperType()
            }
        } else {
            defaultSuperType()
        }
        val superType = when (model.originalCategory) {
            TypeCategory.BOOLEAN, TypeCategory.STRING -> pathType.asTypeName()
            else -> pathType.parameterizedBy(model)
        }
        return TypeSpec.classBuilder(mappings.getPathClassName(model, model))
            .addAnnotations(model.annotations.map { AnnotationSpec.get(it) })
            .addAnnotation(AnnotationSpec.builder(generatedAnnotationClass).addMember("%S", javaClass.name).build())
            .superclass(superType)
            .addType(introCompanion(model, config))
    }

    protected open fun defaultSuperType(): KClass<out Path<*>> = EntityPathBase::class

    protected open fun introCompanion(model: EntityType, config: SerializerConfig): TypeSpec {
        return TypeSpec.companionObjectBuilder()
            .addProperty(PropertySpec.builder("serialVersionUID", Long::class, KModifier.CONST, KModifier.PRIVATE).initializer("%L", model.fullName.hashCode()).build())
            .let { if (config.createDefaultVariable()) it.introDefaultInstance(model, config.defaultVariableName()) else it }
            .build()
    }

    protected open fun TypeSpec.Builder.introJavadoc(model: EntityType, config: SerializerConfig): TypeSpec.Builder = apply {
        addKdoc("%L is a Querydsl query type for %L", mappings.getPathType(model, model, true).simpleName, model.simpleName)
    }

    protected open fun TypeSpec.Builder.introDefaultInstance(model: EntityType, defaultName: String): TypeSpec.Builder = apply {
        val simpleName = defaultName.ifEmpty { model.modifiedSimpleName }
        val queryType = mappings.getPathClassName(model, model)
        val alias = if (keywords.contains(simpleName.uppercase(Locale.getDefault()))) "${simpleName}1" else simpleName
        addProperty(PropertySpec.builder(simpleName, queryType, KModifier.PUBLIC).initializer("%T(%S)", queryType, alias).addAnnotation(JvmField::class).build())
    }

    protected open fun TypeSpec.Builder.introSuper(model: EntityType): TypeSpec.Builder = apply {
        val superType = model.superType?.entityType
        if (superType != null) {
            val superQueryType = mappings.getPathTypeName(superType, model)
            addProperty(
                PropertySpec.builder("_super", superQueryType, KModifier.PUBLIC)
                    .delegate(buildCodeBlock {
                        beginControlFlow("lazy")
                        addStatement("%T(this)", superQueryType)
                        endControlFlow()
                    }).build()
            )
        }
    }

    protected open fun TypeSpec.Builder.serializeProperties(model: EntityType, config: SerializerConfig): TypeSpec.Builder = apply {
        model.properties.forEach { property ->
            // FIXME : the custom types should have the custom type category
            if (mappings.isRegistered(property.type) && property.type.category != TypeCategory.CUSTOM && property.type.category != TypeCategory.ENTITY) {
                customField(model, property, config)
            } else {
                // strips of "? extends " etc
                val queryType = mappings.getPathTypeName(SimpleType(property.type, property.type.parameters), model)
                val classStatement = property.type.asClassNameStatement()

                when (property.type.category ?: TypeCategory.ENTITY) {
                    TypeCategory.STRING -> serialize(model, property, queryType, "createString")
                    TypeCategory.BOOLEAN -> serialize(model, property, queryType, "createBoolean")
                    TypeCategory.SIMPLE -> serialize(model, property, queryType, "createSimple", classStatement)
                    TypeCategory.COMPARABLE -> serialize(model, property, queryType, "createComparable", classStatement)
                    TypeCategory.ENUM -> serialize(model, property, queryType, "createEnum", classStatement)
                    TypeCategory.DATE -> serialize(model, property, queryType, "createDate", classStatement)
                    TypeCategory.DATETIME -> serialize(model, property, queryType, "createDateTime", classStatement)
                    TypeCategory.TIME -> serialize(model, property, queryType, "createTime", classStatement)
                    TypeCategory.NUMERIC -> serialize(model, property, queryType, "createNumber", classStatement)
                    TypeCategory.CUSTOM -> customField(model, property, config)
                    TypeCategory.ARRAY -> serialize(model, property, ArrayPath::class.parameterizedBy(property.type, property.type.componentType), "createArray", classStatement)
                    TypeCategory.COLLECTION -> collectionField(model, property, "createCollection", CollectionPath::class)
                    TypeCategory.SET -> collectionField(model, property, "createSet", SetPath::class)
                    TypeCategory.LIST -> collectionField(model, property, "createList", ListPath::class)
                    TypeCategory.MAP -> {
                        val genericQueryType = mappings.getPathType(getRaw(property.getParameter(1)), model, false)
                        val qType = mappings.getPathClassName(property.getParameter(1), model)
                        serialize(
                            model, property, MapPath::class.parameterizedBy(getRaw(property.getParameter(0)), getRaw(property.getParameter(1)), genericQueryType),
                            CodeBlock.of("this.createMap<%T, %T, %T>", property.getParameter(0).asTypeName(), property.getParameter(1).asTypeName(), genericQueryType.asTypeName()),
                            property.getParameter(0).asClassNameStatement(), property.getParameter(1).asClassNameStatement(), qType.asClassStatement()
                        )
                    }
                    TypeCategory.ENTITY -> entityField(model, property, config)
                }
            }
        }
    }

    private fun TypeSpec.Builder.collectionField(model: EntityType, property: Property, factoryMethod: String, pathClass: KClass<out CollectionPathBase<*, *, *>>) {
        val genericQueryType = mappings.getPathType(getRaw(property.getParameter(0)), model, false)
        val qType = mappings.getPathClassName(property.getParameter(0), model)
        serialize(
            model, property, pathClass.parameterizedBy(getRaw(property.getParameter(0)), genericQueryType),
            CodeBlock.of("this.%L<%T, %T>", factoryMethod, property.getParameter(0).asTypeName(), genericQueryType.asTypeName()),
            property.getParameter(0).asClassNameStatement(), qType.asClassStatement(), "null".asCodeBlock()
        )
    }

    protected open fun TypeSpec.Builder.customField(model: EntityType, field: Property, config: SerializerConfig) {
        val queryType = mappings.getPathTypeName(field.type, model)
        val builder = PropertySpec.builder(field.escapedName, queryType, KModifier.PUBLIC).addKdoc("custom")
        if (field.isInherited) {
            builder.addKdoc("inherited")
            builder.delegate(buildCodeBlock {
                beginControlFlow("lazy")
                addStatement("%T(_super.%L)", queryType, field.escapedName)
                endControlFlow()
            })
        } else {
            builder.initializer("%T(forProperty(%S))", queryType, field.name)
        }
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.serialize(model: EntityType, field: Property, type: TypeName, factoryMethod: Any, vararg args: CodeBlock) {
        val superType = model.superType
        val builder = PropertySpec.builder(field.escapedName, type, KModifier.PUBLIC)
        if (field.isInherited && superType != null) {
            builder.delegate(buildCodeBlock {
                beginControlFlow("lazy")
                addStatement("_super.%L", field.escapedName)
                endControlFlow()
            })
        } else {
            builder.initializer("%L(%S${", %L".repeat(args.size)})", factoryMethod, field.name, *args)
        }
        if (field.isInherited) {
            builder.addKdoc("inherited")
        }
        addProperty(builder.build())
    }

    private fun getRaw(type: Type): Type {
        return if (type is EntityType && type.getPackageName().startsWith("ext.java")) {
            type
        } else {
            SimpleType(type, type.parameters)
        }
    }

    protected open fun TypeSpec.Builder.entityField(model: EntityType, field: Property, config: SerializerConfig) {
        val fieldType = mappings.getPathTypeName(field.type, model)
        val builder = PropertySpec.builder(field.escapedName, fieldType)
        if (field.isInherited) {
            builder.addKdoc("inherited")
        }
        builder.addModifiers(if (config.useEntityAccessors()) KModifier.PROTECTED else KModifier.PUBLIC)
        builder.delegate(buildCodeBlock {
            beginControlFlow("lazy")
            addStatement("%T(forProperty(%S))", fieldType, field.name)
            endControlFlow()
        })
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.constructors(model: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)

        // String
        constructorsForVariables(model)

        // Path
        val path = ParameterSpec.builder("path", if (model.isFinal) Path::class.parameterizedBy(model) else Path::class.parameterizedBy(model.asOutTypeName())).build()
        val pathConstructor = FunSpec.constructorBuilder().addParameter(path)
        if (stringOrBoolean) {
            pathConstructor.callSuperConstructor(CodeBlock.of("%N.metadata", path))
        } else {
            pathConstructor.callSuperConstructor(CodeBlock.of("%N.type", path), CodeBlock.of("%N.metadata", path))
        }
        pathConstructor.addCode(constructorContent(model))
        addFunction(pathConstructor.build())

        // PathMetadata
        val metadata = ParameterSpec.builder("metadata", PathMetadata::class).build()
        val pathMetadataConstructor = FunSpec.constructorBuilder().addParameter(metadata)
        if (stringOrBoolean) {
            pathMetadataConstructor.callSuperConstructor(metadata.asCodeBlock())
        } else {
            pathMetadataConstructor.callSuperConstructor(model.asClassNameStatement(), metadata.asCodeBlock())
        }
        pathConstructor.addCode(constructorContent(model))
        addFunction(pathMetadataConstructor.build())

        // Class, PathMetadata
        val type = ParameterSpec.builder("type", Class::class.asTypeName().parameterizedBy(WildcardTypeName.producerOf(model.asTypeName()))).build()
        addFunction(
            FunSpec.constructorBuilder()
                .addParameter(type)
                .addParameter(metadata)
                .callSuperConstructor(type.asCodeBlock(), metadata.asCodeBlock())
                .addCode(constructorContent(model)).build()
        )
        return this
    }

    protected open fun constructorContent(model: EntityType): CodeBlock {
        // override in subclasses
        return CodeBlock.builder().build()
    }

    protected open fun TypeSpec.Builder.constructorsForVariables(model: EntityType) {
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)

        val variable = ParameterSpec.builder("variable", String::class).build()
        val builder = FunSpec.constructorBuilder().addParameter(variable)
        if (stringOrBoolean) {
            builder.callSuperConstructor(CodeBlock.of("forVariable(%N)", variable))
        } else {
            builder.callSuperConstructor(model.asClassNameStatement(), CodeBlock.of("forVariable(%N)", variable))
        }
        builder.addCode(constructorContent(model))
        addFunction(builder.build())
    }
}
