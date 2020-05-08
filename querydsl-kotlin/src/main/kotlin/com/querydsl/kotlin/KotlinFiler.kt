package com.querydsl.kotlin

import com.querydsl.codegen.Filer
import java.io.File
import java.lang.Appendable
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class KotlinFiler : Filer {
    override fun createFile(processingEnvironment: ProcessingEnvironment, classname: String, elements: MutableCollection<out Element>): Appendable {
        val file = File("${processingEnvironment.options["kapt.kotlin.generated"]}/${classname.replace(".", "/")}.kt")
        file.parentFile.mkdirs()
        return file.writer()
    }
}