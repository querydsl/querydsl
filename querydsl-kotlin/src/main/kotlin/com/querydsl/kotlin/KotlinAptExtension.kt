package com.querydsl.kotlin

import com.querydsl.apt.Extension
import com.querydsl.codegen.AbstractModule
import com.querydsl.codegen.EmbeddableSerializer
import com.querydsl.codegen.EntitySerializer
import com.querydsl.codegen.Filer
import com.querydsl.codegen.ProjectionSerializer
import com.querydsl.codegen.SupertypeSerializer
import com.querydsl.codegen.TypeMappings

class KotlinAptExtension : Extension {
    override fun addSupport(module: AbstractModule) {
        module.bind(EntitySerializer::class.java, KotlinEntitySerializer::class.java)
        module.bind(EmbeddableSerializer::class.java, KotlinEmbeddableSerializer::class.java)
        module.bind(SupertypeSerializer::class.java, KotlinSuperSerializer::class.java)
        module.bind(ProjectionSerializer::class.java, KotlinProjectionSerializer::class.java)
        module.bind(TypeMappings::class.java, KotlinTypeMappings::class.java)
        module.bind(Filer::class.java, KotlinFiler::class.java)
    }
}