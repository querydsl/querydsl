package com.querydsl.scala

import java.io.File

import com.querydsl.codegen.GenericExporter
import org.junit.Test

class GenericExporterTest {

  @Test
  def Export {
    val exporter = new GenericExporter()
    exporter.setTargetFolder(new java.io.File("target/gen1"))
    exporter.setSerializerClass(classOf[ScalaEntitySerializer])
    exporter.setTypeMappingsClass(classOf[ScalaTypeMappings])
    exporter.setCreateScalaSources(true)
    exporter.export(getClass.getPackage)

    val targetFolder = new File("target/gen1")
    CompileTestUtils.assertCompileSuccess(targetFolder)
  }

}


