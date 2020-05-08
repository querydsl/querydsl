package com.querydsl.kotlin

import com.mysema.codegen.CodeWriter
import com.mysema.codegen.model.ClassType
import com.mysema.codegen.model.SimpleType
import com.mysema.codegen.model.Type
import com.mysema.codegen.model.TypeCategory
import com.mysema.codegen.model.TypeExtends
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
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.DatePath
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.core.types.dsl.EntityPathBase
import com.querydsl.core.types.dsl.EnumPath
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

open class KotlinEntitySerializer @Inject constructor(protected val mappings: TypeMappings, protected @Named("keywords") val keyword: Collection<String>) : EntitySerializer {

    override fun serialize(type: EntityType, config: SerializerConfig, writer: CodeWriter) {
        val queryType: Type = mappings.getPathType(type, type, false)
        FileSpec.builder(queryType.packageName, queryType.simpleName)
                .addImport(PathMetadataFactory::class, "forVariable", "forProperty")
                .addType(intro(type, config)
                        .serializeProperties(type, config)
                        .constructors(type, config)
                        .build())
                .build()
                .writeTo(writer)

    }

    protected open fun intro(type: EntityType, config: SerializerConfig): TypeSpec.Builder {
        return introClassHeader(type, config)
                .introJavadoc(type, config)
                .let { if (type.superType?.entityType != null) it.introSuper(type) else it }
    }

    protected open fun introClassHeader(type: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val queryType = mappings.getPathType(type, type, true)

        val category: TypeCategory = type.originalCategory
        val pathType = if (type.properties.isEmpty()) {
            when (category) {
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
        val superType = if (category == TypeCategory.BOOLEAN || category == TypeCategory.STRING) {
            pathType.asTypeName()
        } else {
            pathType.asTypeName().parameterizedBy(type.asTypeName())
        }
        return TypeSpec.classBuilder(queryType.asClassName())
                .addAnnotations(type.annotations.map { AnnotationSpec.get(it) })
                .addAnnotation(AnnotationSpec.builder(Generated::class.java).addMember("%S", javaClass.name).build())
                .superclass(superType)
                .addType(introCompanion(type, config))
    }

    protected open fun defaultSuperType(): KClass<out Path<*>> = EntityPathBase::class

    protected open fun introCompanion(type: EntityType, config: SerializerConfig): TypeSpec {
        return TypeSpec.companionObjectBuilder()
                .addProperty(PropertySpec.builder("serialVersionUID", Long::class, KModifier.CONST, KModifier.PRIVATE).initializer("%L", type.fullName.hashCode()).build())
                .introInits(type, config)
                .let { if (config.createDefaultVariable()) it.introDefaultInstance(type, config.defaultVariableName()) else it }
                .build()
    }

    protected open fun TypeSpec.Builder.introInits(type: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val inits = type.properties.flatMap { property -> property.inits.map { "${property.escapedName}.$it" } }
        if (inits.isNotEmpty()) {
            addProperty(PropertySpec.builder("INITS", PathInits::class, KModifier.PRIVATE).initializer("%T(${"%S, ".repeat(inits.size)}%S)", PathInits::class, *inits.toTypedArray(), "*").build())
        } else if (type.hasEntityFields() || superTypeHasEntityFields(type)) {
            addProperty(PropertySpec.builder("INITS", PathInits::class, KModifier.PRIVATE).initializer("%T.DIRECT2", PathInits::class).build())
        }
        return this
    }

    private fun superTypeHasEntityFields(type: EntityType): Boolean {
        return type.superType?.entityType?.hasEntityFields() ?: false
    }

    protected open fun TypeSpec.Builder.introJavadoc(type: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val queryType: Type = mappings.getPathType(type, type, true)
        addKdoc("%L is a Querydsl query type for %L", queryType.simpleName, type.simpleName)
        return this
    }

    protected open fun TypeSpec.Builder.introDefaultInstance(type: EntityType, defaultName: String): TypeSpec.Builder {
        val simpleName = if (defaultName.isNotEmpty()) defaultName else type.modifiedSimpleName
        val queryType: Type = mappings.getPathType(type, type, true)
        var alias = simpleName
        if (keyword.contains(simpleName.toUpperCase())) {
            alias += "1"
        }
        addProperty(PropertySpec.builder(simpleName, queryType.asTypeName(), KModifier.PUBLIC).initializer("%T(%S)", queryType.asTypeName(), alias).build())
        return this
    }

    protected open fun TypeSpec.Builder.introSuper(type: EntityType): TypeSpec.Builder {
        val superType = type.superType?.entityType
        val superQueryType = mappings.getPathType(superType, type, false)
        if (superType?.hasEntityFields() == false) {
            addProperty(PropertySpec.builder("_super", superQueryType.asTypeName(), KModifier.PUBLIC).initializer("%T(this)", superQueryType.asTypeName()).build())
        } else {
            addProperty(PropertySpec.builder("_super", superQueryType.asTypeName(), KModifier.PUBLIC).build())
        }
        return this
    }

    protected open fun TypeSpec.Builder.serializeProperties(type: EntityType, config: SerializerConfig): TypeSpec.Builder {
        type.properties.forEach { property ->
            // FIXME : the custom types should have the custom type category
            if (mappings.isRegistered(property.type) && property.type.category != TypeCategory.CUSTOM && property.type.category != TypeCategory.ENTITY) {
                customField(type, property, config)
            } else {
                // strips of "? extends " etc
                val propertyType: Type = SimpleType(property.type, property.type.parameters)
                var queryType: Type = mappings.getPathType(propertyType, type, false)
                val genericQueryType: Type
                val localRawName = property.type
                val inits = getInits(property)

                when (property.type.category ?: TypeCategory.ENTITY) {
                    TypeCategory.STRING -> serialize(type, property, queryType.asTypeName(), "createString")
                    TypeCategory.BOOLEAN -> serialize(type, property, queryType.asTypeName(), "createBoolean")
                    TypeCategory.SIMPLE -> serialize(type, property, queryType.asTypeName(), "createSimple", localRawName.asClassName().asClassStatement())
                    TypeCategory.COMPARABLE -> serialize(type, property, queryType.asTypeName(), "createComparable", localRawName.asClassName().asClassStatement())
                    TypeCategory.ENUM -> serialize(type, property, queryType.asTypeName(), "createEnum", localRawName.asClassName().asClassStatement())
                    TypeCategory.DATE -> serialize(type, property, queryType.asTypeName(), "createDate", localRawName.asClassName().asClassStatement())
                    TypeCategory.DATETIME -> serialize(type, property, queryType.asTypeName(), "createDateTime", localRawName.asClassName().asClassStatement())
                    TypeCategory.TIME -> serialize(type, property, queryType.asTypeName(), "createTime", localRawName.asClassName().asClassStatement())
                    TypeCategory.NUMERIC -> serialize(type, property, queryType.asTypeName(), "createNumber", localRawName.asClassName().asClassStatement())
                    TypeCategory.CUSTOM -> customField(type, property, config)
                    TypeCategory.ARRAY -> serialize(type, property, ArrayPath::class.asTypeName().parameterizedBy(property.type.asTypeName(), property.type.componentType.asTypeName()),
                            "createArray", localRawName.asClassName().asClassStatement())
                    TypeCategory.COLLECTION -> {
                        genericQueryType = mappings.getPathType(getRaw(property.getParameter(0)), type, false)
                        queryType = mappings.getPathType(property.getParameter(0), type, true)
                        serialize(type, property, CollectionPath::class.asTypeName().parameterizedBy(getRaw(property.getParameter(0)).asTypeName(), genericQueryType.asTypeName()),
                                CodeBlock.of("this.createCollection<%T, %T>", property.getParameter(0).asTypeName(), genericQueryType.asTypeName()),
                                property.getParameter(0).asClassName().asClassStatement(), queryType.asClassName().asClassStatement(), inits)
                    }
                    TypeCategory.SET -> {
                        genericQueryType = mappings.getPathType(getRaw(property.getParameter(0)), type, false)
                        queryType = mappings.getPathType(property.getParameter(0), type, true)
                        serialize(type, property, SetPath::class.asTypeName().parameterizedBy(getRaw(property.getParameter(0)).asTypeName(), genericQueryType.asTypeName()),
                                CodeBlock.of("this.createSet<%T, %T>", property.getParameter(0).asTypeName(), genericQueryType.asTypeName()),
                                property.getParameter(0).asClassName().asClassStatement(), queryType.asClassName().asClassStatement(), inits)
                    }
                    TypeCategory.LIST -> {
                        genericQueryType = mappings.getPathType(getRaw(property.getParameter(0)), type, false)
                        queryType = mappings.getPathType(property.getParameter(0), type, true)
                        serialize(type, property, SetPath::class.asTypeName().parameterizedBy(getRaw(property.getParameter(0)).asTypeName(), genericQueryType.asTypeName()),
                                CodeBlock.of("this.createList<%T, %T>", property.getParameter(0).asTypeName(), genericQueryType.asTypeName()),
                                property.getParameter(0).asClassName().asClassStatement(), queryType.asClassName().asClassStatement(), inits)
                    }
                    TypeCategory.MAP -> {
                        genericQueryType = mappings.getPathType(getRaw(property.getParameter(1)), type, false)
                        queryType = mappings.getPathType(property.getParameter(1), type, true)
                        serialize(type, property, MapPath::class.asTypeName().parameterizedBy(getRaw(property.getParameter(0)).asTypeName(),
                                getRaw(property.getParameter(1)).asTypeName(), genericQueryType.asTypeName()),
                                CodeBlock.of("this.createMap<%T, %T, %T>", property.getParameter(0).asTypeName(), property.getParameter(1).asTypeName(), genericQueryType.asTypeName()),
                                property.getParameter(0).asClassName().asClassStatement(), property.getParameter(1).asClassName().asClassStatement(),
                                queryType.asClassName().asClassStatement())
                    }
                    TypeCategory.ENTITY -> entityField(type, property, config)
                }
            }
        }
        return this
    }

    private fun getInits(property: Property): CodeBlock {
        return if (property.inits.isNotEmpty()) {
            CodeBlock.of("INITS.get(%S)", property.name)
        } else {
            CodeBlock.of("%T.DIRECT2", PathInits::class)
        }
    }

    protected open fun TypeSpec.Builder.customField(type: EntityType, field: Property, config: SerializerConfig) {
        val queryType = mappings.getPathType(field.type, type, false)
        val builder = PropertySpec.builder(field.escapedName, queryType.asTypeName(), KModifier.PUBLIC).addKdoc("custom")
        if (field.isInherited) {
            builder.addKdoc("inherited")
            val superType = type.superType
            if (superType?.entityType?.hasEntityFields() == false) {
                builder.initializer("%T(_super.%L)", queryType.asTypeName(), field.escapedName)
            }
        } else {
            builder.initializer("%T(forProperty(%S)", queryType.asTypeName(), field.name)
        }
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.serialize(model: EntityType, field: Property, type: TypeName, factoryMethod: Any, vararg args: Any) {
        val superType = model.superType
        val builder = PropertySpec.builder(field.escapedName, type, KModifier.PUBLIC)
        val initializer = CodeBlock.builder()
        if (field.isInherited && superType != null) {
            if (superType.entityType?.hasEntityFields() == false) {
                initializer.add("_super.%L", field.escapedName)
            }
        } else {
            initializer.add("%L(%S${", %L".repeat(args.size)})", factoryMethod, field.name, *args)
        }
        builder.initializer(initializer.build())

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
        val queryType: Type = mappings.getPathType(field.type, model, false)
        val builder = PropertySpec.builder(field.escapedName, queryType.asTypeName())
        if (field.isInherited) {
            builder.addKdoc("inherited")
        }
        if (config.useEntityAccessors()) {
            builder.addModifiers(KModifier.PROTECTED)
        } else {
            builder.addModifiers(KModifier.PUBLIC)
        }
        addProperty(builder.build())
    }

    protected open fun TypeSpec.Builder.constructors(model: EntityType, config: SerializerConfig): TypeSpec.Builder {
        val localName = model
        val hasEntityFields = model.hasEntityFields() || superTypeHasEntityFields(model)
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)


        // String
        constructorsForVariables(model)

        // Path
        val simpleModel: Type = SimpleType(model)
        val pathConstructor = FunSpec.constructorBuilder()
        if (model.isFinal) {
            val type: Type = ClassType(Path::class.java, simpleModel)
            pathConstructor.addParameter("path", type.asTypeName())
        } else {
            val type: Type = ClassType(Path::class.java, TypeExtends(simpleModel))
            pathConstructor.addParameter("path", type.asTypeName())
        }
        if (!hasEntityFields) {
            if (stringOrBoolean) {
                pathConstructor.callSuperConstructor("path.metadata")
            } else {
                pathConstructor.callSuperConstructor("path.type", "path.metadata")
            }
            pathConstructor.addCode(constructorContent(model))
        } else {
            pathConstructor.callThisConstructor(CodeBlock.of("path.type"), CodeBlock.of("path.metadata"), CodeBlock.of("%T.getFor(path.metadata, INITS)", PathInits::class))
        }
        addFunction(pathConstructor.build())

        // PathMetadata
        val pathMetadataConstructor = FunSpec.constructorBuilder()
                .addParameter("metadata", PathMetadata::class)
        if (hasEntityFields) {
            pathMetadataConstructor.callThisConstructor(CodeBlock.of("metadata"), CodeBlock.of("%T.getFor(metadata, INITS)", PathInits::class))
        } else {
            if (stringOrBoolean) {
                pathMetadataConstructor.callSuperConstructor("metadata")
            } else {
                pathMetadataConstructor.callSuperConstructor(localName.asClassName().asClassStatement(), CodeBlock.of("metadata"))
            }
            pathConstructor.addCode(constructorContent(model))
        }
        addFunction(pathMetadataConstructor.build())

        // PathMetadata, PathInits
        if (hasEntityFields) {
            val builder = FunSpec.constructorBuilder()
                    .addParameter("metadata", PathMetadata::class)
                    .addParameter("inits", PathInits::class)
            if (hasEntityFields) {
                builder.callThisConstructor(localName.asClassName().asClassStatement(), CodeBlock.of("metadata"), CodeBlock.of("inits"))
            } else {
                builder.callSuperConstructor(localName.asClassName().asClassStatement(), CodeBlock.of("metadata"), CodeBlock.of("inits"))
            }
            addFunction(builder.build())
        }

        // Class, PathMetadata, PathInits
        if (hasEntityFields) {
            val builder = FunSpec.constructorBuilder()
                    .addParameter("type", Class::class.asTypeName().parameterizedBy(WildcardTypeName.producerOf(model.asTypeName())))
                    .addParameter("metadata", PathMetadata::class)
                    .addParameter("inits", PathInits::class)
                    .callSuperConstructor("type", "metadata", "inits")
                    .addCode(initEntityFields(config, model))
                    .addCode(constructorContent(model))
            addFunction(builder.build())
        }
        return this
    }

    protected open fun initEntityFields(config: SerializerConfig, model: EntityType): CodeBlock {
        val superType = model.superType
        val builder = CodeBlock.builder()
        if (superType?.entityType?.hasEntityFields() == true) {
            val superQueryType: Type = mappings.getPathType(superType.entityType, model, false)
            builder.addStatement("this._super = %T(type, metadata, inits)", superQueryType.asTypeName())
        }
        for (field in model.properties) {
            if (field.type.category == TypeCategory.ENTITY) {
                builder.initEntityField(config, model, field)
            } else if (field.isInherited && superType?.entityType?.hasEntityFields() == true) {
                builder.addStatement("this.%1L = _super.%1L", field.escapedName)
            }
        }
        return builder.build()
    }

    protected open fun CodeBlock.Builder.initEntityField(config: SerializerConfig, model: EntityType, field: Property) {
        val queryType: Type = mappings.getPathType(field.type, model, false)
        if (!field.isInherited) {
            val hasEntityFields = (field.type is EntityType && (field.type as EntityType).hasEntityFields())
            if (hasEntityFields) {
                addStatement("this.%1L = if (inits.isInitialized(%2S)) %3T(forProperty(%2S), inits.get(%2S)) else throw %4T()",
                        field.escapedName, field.name, queryType.asTypeName(), IllegalStateException::class)
            } else {
                addStatement("this.%1L = if (inits.isInitialized(%2S)) %3T(forProperty(%2S)) else throw %4T()",
                        field.escapedName, field.name, queryType.asTypeName(), IllegalStateException::class)
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
        val localName = model
        val stringOrBoolean = (model.originalCategory == TypeCategory.STRING || model.originalCategory == TypeCategory.BOOLEAN)
        val hasEntityFields = model.hasEntityFields() || superTypeHasEntityFields(model)

        val builder = FunSpec.constructorBuilder()
                .addParameter("variable", String::class)
        if (stringOrBoolean) {
            if (hasEntityFields) {
                builder.callThisConstructor("forVariable(variable)")
            } else {
                builder.callSuperConstructor("forVariable(variable)")
            }
        } else {
            if (hasEntityFields) {
                builder.callThisConstructor(localName.asClassName().asClassStatement(), CodeBlock.of("forVariable(variable)"), CodeBlock.of("INITS"))
            } else {
                builder.callSuperConstructor(localName.asClassName().asClassStatement(), CodeBlock.of("forVariable(variable)"))
            }
        }
        if (!hasEntityFields) {
            builder.addCode(constructorContent(model))
        }
        addFunction(builder.build())
    }
}
