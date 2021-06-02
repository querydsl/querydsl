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
import com.querydsl.codegen.EmbeddableSerializer
import com.querydsl.codegen.GeneratedAnnotationResolver
import com.querydsl.codegen.TypeMappings
import com.querydsl.core.types.Path
import com.querydsl.core.types.dsl.BeanPath
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

class KotlinEmbeddableSerializer @Inject constructor(
    mappings: TypeMappings,
    @Named(CodegenModule.KEYWORDS)
    keywords: Collection<String>,
    @Named(CodegenModule.GENERATED_ANNOTATION_CLASS)
    generatedAnnotationClass: Class<out Annotation> = GeneratedAnnotationResolver.resolveDefault()
) : KotlinEntitySerializer(mappings, keywords, generatedAnnotationClass), EmbeddableSerializer {
    override fun defaultSuperType(): KClass<out Path<*>> = BeanPath::class
}