package com.querydsl.kotlin

import com.querydsl.codegen.EmbeddableSerializer
import com.querydsl.codegen.TypeMappings
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.BeanPath
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

class KotlinEmbeddableSerializer @Inject constructor(mappings: TypeMappings, @Named("keywords") keyword: Collection<String>) : KotlinEntitySerializer(mappings, keyword), EmbeddableSerializer {
    override fun defaultSuperType(): KClass<out Path<*>> = BeanPath::class
}