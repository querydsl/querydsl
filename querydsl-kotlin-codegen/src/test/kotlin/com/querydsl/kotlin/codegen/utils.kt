package com.querydsl.kotlin.codegen

import com.querydsl.codegen.GeneratedAnnotationResolver

val generatedAnnotationImport = GeneratedAnnotationResolver.resolveDefault().name.replace("annotation", "`annotation`")