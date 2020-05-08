package com.querydsl.kotlin

import com.querydsl.codegen.SupertypeSerializer
import com.querydsl.codegen.TypeMappings
import javax.inject.Inject
import javax.inject.Named

class KotlinSuperSerializer @Inject constructor(mappings: TypeMappings, @Named("keywords") keyword: Collection<String>) : KotlinEntitySerializer(mappings, keyword), SupertypeSerializer {
}