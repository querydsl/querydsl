package com.mysema.query.scala;
import com.mysema.query.annotations._
import com.mysema.query.codegen.GenericExporter
import org.junit.Test

class GenericExporterTest extends CompileTestUtils {
    
  @Test
  def Export() {
    val exporter = new GenericExporter()
    exporter.setTargetFolder(new java.io.File("target/gen1"))
    exporter.setSerializerClass(classOf[ScalaEntitySerializer])
    exporter.setCreateScalaSources(true)
    exporter.export(getClass.getPackage)
    
    val targetFolder = new java.io.File("target/gen1/com/mysema/query/scala/") 
    val sources = targetFolder listFiles() filter( _.getName.endsWith(".scala") ) map( io.Source.fromFile(_).mkString ) mkString("\n")
    assertCompileSuccess(sources)
  }

}

@QuerySupertype
class Superclass {
    
  var id: Int = _
    
}

@QueryEntity
class EntityClass extends Superclass {
    
    // FIXME
//  var comparable: Comparable[_] = _   
    
  var code: String = _  

  var stringList: java.util.List[String] = _
  
  var embedded: EmbeddableClass = _
  
}

@QueryEntity
class EntitySubclass extends EntityClass {
 
  var property: String = _ 
    
}

@QueryEmbeddable
class EmbeddableClass {

  var property: String = _  
    
}

