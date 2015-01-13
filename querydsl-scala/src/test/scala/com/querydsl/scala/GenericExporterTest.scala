package com.querydsl.scala;

import com.querydsl.core.annotations._
import com.querydsl.codegen.GenericExporter
import org.junit.Test
import io.Source.fromFile
import java.io.File

class GenericExporterTest extends CompileTestUtils {

  @Test
  def Export {
    val exporter = new GenericExporter()
    exporter.setTargetFolder(new java.io.File("target/gen1"))
    exporter.setSerializerClass(classOf[ScalaEntitySerializer])
    exporter.setTypeMappingsClass(classOf[ScalaTypeMappings])
    exporter.setCreateScalaSources(true)
    exporter.export(getClass.getPackage)

    val targetFolder = new File("target/gen1/com/mysema/querydsl/scala/")
    
    // com.querydsl.scala
    val sources = (targetFolder.listFiles()
      filter (_.getName.endsWith(".scala"))
      map (fromFile(_).mkString)
      mkString ("\n"))
    assertCompileSuccess(sources)
    
    // com.querydsl.scala.ext
    val other = (new File(targetFolder, "ext").listFiles()
      filter (_.getName.endsWith(".scala"))
      map (fromFile(_).mkString)
      mkString ("\n"))
    assertCompileSuccess("import com.querydsl.scala.ext._\n" + other)
  }
  
}


