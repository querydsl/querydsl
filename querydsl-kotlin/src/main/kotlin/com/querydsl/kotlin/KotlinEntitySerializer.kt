package com.querydsl.kotlin

import com.mysema.codegen.CodeWriter
import com.mysema.codegen.model.SimpleType
import com.mysema.codegen.model.Type
import com.mysema.codegen.model.TypeCategory
import com.querydsl.codegen.EntitySerializer
import com.querydsl.codegen.EntityType
import com.querydsl.codegen.Property
import com.querydsl.codegen.SerializerConfig
import com.querydsl.codegen.TypeMappings
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
import com.querydsl.core.types.dsl.PathInits
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
import javax.annotation.Generated
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

open class KotlinEntitySerializer @Inject constructor(protected val mappings: TypeMappings, protected @Named("keywords") val keywords: Collection<String>) : EntitySerializer {

    override fun serialize(model: EntityType, config: SerializerConfig, writer: CodeWriter) {
        val queryType: Type = mappings.getPathType(model, model, false)
        FileSpec.builder(queryType.packageName, queryType.simpleName)
                .addImport(PathMetadataFactory::class, "forVariable", "forProperty")
                .addType(intro(model, config)
                        .serializeProperties(model, config)
                        .constructors(model, config)
                        .build())
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
                .addAnnotation(AnnotationSpec.builder(Generated::class.java).addMember("%S", javaClass.name).build())
                .superclass(superType)
                .addType(introCompanion(model, config))
    }

    protected open fun defaultSuperType(): KClass<out Path<*>> = EntityPathBase::class

    protected open fun introCompanion(model: EntityType, config: SerializerConfig): TypeSpec {
        return TypeSpec.companionObjectBuilder()
                .addProperty(PropertySpec.builder("serialVersionUID", Long::class, KModifier.CONST, KModifier.PRIVATE).initializer("%L", model.fullName.hashCode()).build())
                .introInits(model, config)
                .let { if (config.createDefaultVariable()) it.introDefaultInstance(model, config.defaultVariableName()) else it }
                .build()
    }

    protected open fun TypeSpec.Builder.introInits(model: EntityType, config: SerializerConfig): TypeSpec.Builder = apply {
        val inits = model.properties.flatMap { property -> property.inits.map { "${property.escapedName}.$it" } }
        val builder = PropertySpec.builder("INITS", PathInits::class, KModifier.PRIVATE)
        if (inits.isNotEmpty()) {
            addProperty(builder.initializer(CodeBlock.of("%T(%L)", PathInits::class, ((inits + "*").joinToCode()))).build())
        } else if (model.hasEntityFields() || superTypeHasEntityFields(model)) {
            addProperty(builder.initializer("%T.DIRECT2", PathInits::class).build())
        }
    }

    private fun superTypeHasEntityFields(model: EntityType): Boolean {
        return model.superType?.entityType?.hasEntityFields() ?: false
    }

    protected open fun TypeSpec.Builder.introJavadoc(model: EntityType, config: SerializerConfig): TypeSpec.Builder = apply {
        addKdoc("%L is a Querydsl query type for %L", mappings.getPathType(model, model, true).simpleName, model.simpleName)
    }

    protected open fun TypeSpec.Builder.introDefaultInstance(model: EntityType, defaultName: String): TypeSpec.Builder = apply {
        val simpleName = if (defaultName.isNotEmpty()) defaultName else model.modifiedSimpleName
        val queryType = mappings.getPathClassName(model, model)
        val alias = if (keywords.contains(simpleName.toUpperCase())) "${simpleName}1" else simpleName
        addProperty(PropertySpec.builder(simpleName, queryType, KModifier.PUBLIC).initializer("%T(%S)", queryType, alias).build())
    }

    protected open fun TypeSpec.Builder.introSuper(model: EntityType): TypeSpec.Builder = apply {
        val superType = model.superType?.entityType
        if (superType != null) {
            val superQueryType = mappings.getPathTypeName(superType, model)
            val builder = PropertySpec.builder("_super", superQueryType, KModifier.PUBLIC)
            if (!superType.hasEntityFields()) {
                builder.initializer("%T(this)", superQueryType)
            }
            addProperty(builder.build())
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
                        serialize(model, property, MapPath::class.parameterizedBy(getRaw(property.getParameter(0)), getRaw(property.getParameter(1)), genericQueryType),
                                CodeBlock.of("this.createMap<%T, %T, %T>", property.getParameter(0).asTypeName(), property.getParameter(1).asTypeName(), genericQueryType.asTypeName()),
                                property.getParameter(0).asClassNameStatement(), property.getParameter(1).asClassNameStatement(), qType.asClassStatement())
                    }
                    TypeCategory.ENTITY -> entityField(model, property, config)
                }
            }
        }
    }

    private fun TypeSpec.Builder.collectionField(model: EntityType, property: Property, factoryMethod: String, pathClass: KClass<out CollectionPathBase<*, *, *>>) {
        val genericQueryType = mappings.getPathType(getRaw(property.getParameter(0)), model, false)
        val qType = mappings.getPathClassName(property.getParameter(0), model)
        serialize(model, property, pathClass.parameterizedBy(getRaw(property.getParameter(0)), genericQueryType),
                CodeBlock.of("this.%L<%T, %T>", factoryMethod, property.getParameter(0).asTypeName(), genericQueryType.asTypeName()),
                property.getParameter(0).asClassNameStatement(), qType.asClassStatement(), getInits(property))
    }

    private fun getInits(property: Property): CodeBlock {
        return if (property.inits.isNotEmpty()) {
            CodeBlock.of("INITS.get(%S)", property.name)
        } else {
            CodeBlock.of("%T.DIRECT2", PathInits::class)
        }
    }

    protected open fun TypeSpec.Builder.customField(model: EntityType, field: Property, config: SerializerConfig) {
        val queryType = mappings.getPathTypeName(field.type, model)
        val builder = PropertySpec.builder(field.escapedName, queryType, KModifier.PUBLIC).addKdoc("custom")
        if (field.isInherited) {
            builder.addKdoc("inherited")
            val superType = model.superType
            if (superType?.entityType?.hasEntityFields() == false) {
                builder.initializer("%T(_super.%L)", queryType, field.escapedName)
            }
        } else {
            builder.initializer("%T(forProperty(%S)", queryType, field.name)
        }
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.serialize(model: EntityType, field: Property, type: TypeName, factoryMethod: Any, vararg args: CodeBlock) {
        val superType = model.superType
        val builder = PropertySpec.builder(field.escapedName, type, KModifier.PUBLIC)
        if (field.isInherited && superType != null) {
            if (superType.entityType?.hasEntityFields() == false) {
                builder.initializer("_super.%L", field.escapedName)
            }
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
        val builder = PropertySpec.builder(field.escapedName, mappings.getPathTypeName(field.type, model))
        if (field.isInherited) {
            builder.addKdoc("inherited")
        }
        builder.addModifiers(if (config.useEntityAccessors()) KModifier.PROTECTED else KModifier.PUBLIC)
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.constructors(model: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val hasEntityFields = model.hasEntityFields() || superTypeHasEntityFields(model)
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)

        // String
        constructorsForVariables(model)

        // Path
        val path = ParameterSpec.builder("path", if (model.isFinal) Path::class.parameterizedBy(model) else Path::class.parameterizedBy(model.asOutTypeName())).build()
        val pathConstructor = FunSpec.constructorBuilder().addParameter(path)
        if (!hasEntityFields) {
            if (stringOrBoolean) {
                pathConstructor.callSuperConstructor(CodeBlock.of("%N.metadata", path))
            } else {
                pathConstructor.callSuperConstructor(CodeBlock.of("%N.type", path), CodeBlock.of("%N.metadata", path))
            }
            pathConstructor.addCode(constructorContent(model))
        } else {
            pathConstructor.callThisConstructor(CodeBlock.of("%N.type", path), CodeBlock.of("%N.metadata", path),
                    CodeBlock.of("%T.getFor(%N.metadata, INITS)", PathInits::class, path))
        }
        addFunction(pathConstructor.build())

        // PathMetadata
        val metadata = ParameterSpec.builder("metadata", PathMetadata::class).build()
        val pathMetadataConstructor = FunSpec.constructorBuilder().addParameter(metadata)
        if (hasEntityFields) {
            pathMetadataConstructor.callThisConstructor(metadata.asCodeBlock(), CodeBlock.of("%T.getFor(%N, INITS)", PathInits::class, metadata))
        } else {
            if (stringOrBoolean) {
                pathMetadataConstructor.callSuperConstructor(metadata.asCodeBlock())
            } else {
                pathMetadataConstructor.callSuperConstructor(model.asClassNameStatement(), metadata.asCodeBlock())
            }
            pathConstructor.addCode(constructorContent(model))
        }
        addFunction(pathMetadataConstructor.build())

        // PathMetadata, PathInits
        val inits = ParameterSpec.builder("inits", PathInits::class).build()
        if (hasEntityFields) {
            val builder = FunSpec.constructorBuilder()
                    .addParameter(metadata)
                    .addParameter(inits)
            if (hasEntityFields) {
                builder.callThisConstructor(model.asClassNameStatement(), metadata.asCodeBlock(), inits.asCodeBlock())
            } else {
                builder.callSuperConstructor(model.asClassNameStatement(), metadata.asCodeBlock(), inits.asCodeBlock())
            }
            addFunction(builder.build())
        }

        // Class, PathMetadata, PathInits
        val type = ParameterSpec.builder("type", Class::class.asTypeName().parameterizedBy(WildcardTypeName.producerOf(model.asTypeName()))).build()
        if (hasEntityFields) {
            addFunction(FunSpec.constructorBuilder()
                    .addParameter(type)
                    .addParameter(metadata)
                    .addParameter(inits)
                    .callSuperConstructor(type.asCodeBlock(), metadata.asCodeBlock(), inits.asCodeBlock())
                    .addCode(initEntityFields(config, model, type, metadata, inits))
                    .addCode(constructorContent(model)).build())
        }
        return this
    }

    protected open fun initEntityFields(config: SerializerConfig, model: EntityType, type : ParameterSpec, metadata: ParameterSpec, inits: ParameterSpec): CodeBlock {
        val builder = CodeBlock.builder()
        val entityType = model.superType?.entityType
        if (entityType?.hasEntityFields() == true) {
            builder.addStatement("this._super = %T(%N, %N, %N)", mappings.getPathTypeName(entityType, model), type, metadata, inits)
        }
        for (field in model.properties) {
            if (field.type.category == TypeCategory.ENTITY) {
                builder.initEntityField(config, model, field, inits)
            } else if (field.isInherited && entityType?.hasEntityFields() == true) {
                builder.addStatement("this.%1L = _super.%1L", field.escapedName)
            }
        }
        return builder.build()
    }

    protected open fun CodeBlock.Builder.initEntityField(config: SerializerConfig, model: EntityType, field: Property, inits: ParameterSpec) {
        val queryType = mappings.getPathTypeName(field.type, model)
        if (!field.isInherited) {
            val hasEntityFields = (field.type is EntityType && (field.type as EntityType).hasEntityFields())
            if (hasEntityFields) {
                addStatement("this.%1L = if (%5N.isInitialized(%2S)) %3T(forProperty(%2S), %5N.get(%2S)) else throw %4T()",
                        field.escapedName, field.name, queryType, IllegalStateException::class, inits)
            } else {
                addStatement("this.%1L = if (%5N.isInitialized(%2S)) %3T(forProperty(%2S)) else throw %4T()",
                        field.escapedName, field, queryType, IllegalStateException::class, inits)
            }
        } else if (!config.useEntityAccessors()) {
            addStatement("this.%1L = _super.%1L", field.escapedName)
        }
    }

    protected open fun constructorContent(model: EntityType): CodeBlock {
        // override in subclasses
        return CodeBlock.builder().build()
    }

    protected open fun TypeSpec.Builder.constructorsForVariables(model: EntityType) {
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)
        val hasEntityFields = model.hasEntityFields() || superTypeHasEntityFields(model)

        val variable = ParameterSpec.builder("variable", String::class).build()
        val builder = FunSpec.constructorBuilder().addParameter(variable)
        if (stringOrBoolean) {
            if (hasEntityFields) {
                builder.callThisConstructor(CodeBlock.of("forVariable(%N)", variable))
            } else {
                builder.callSuperConstructor(CodeBlock.of("forVariable(%N)", variable))
            }
        } else {
            if (hasEntityFields) {
                builder.callThisConstructor(model.asClassNameStatement(), CodeBlock.of("forVariable(%N)", variable), CodeBlock.of("INITS"))
            } else {
                builder.callSuperConstructor(model.asClassNameStatement(), CodeBlock.of("forVariable(%N)", variable))
            }
        }
        if (!hasEntityFields) {
            builder.addCode(constructorContent(model))
        }
        addFunction(builder.build())
    }
}
