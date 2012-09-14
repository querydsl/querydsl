package com.mysema.query.scala;

import com.mysema.query.annotations._
import com.mysema.query.codegen.GenericExporter
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

    val targetFolder = new File("target/gen1/com/mysema/query/scala/")
    
    // com.mysema.query.scala 
    val sources = (targetFolder.listFiles()
      filter (_.getName.endsWith(".scala"))
      map (fromFile(_).mkString)
      mkString ("\n"))
    assertCompileSuccess(sources)
    
    // com.mysema.query.scala.ext
    val other = (new File(targetFolder, "ext").listFiles()
      filter (_.getName.endsWith(".scala"))
      map (fromFile(_).mkString)
      mkString ("\n"))
    assertCompileSuccess("import com.mysema.query.scala.ext._\n" + other)
  }
  
}


