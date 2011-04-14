package com.mysema.query.scala;
import com.mysema.query.annotations._
import com.mysema.query.codegen.GenericExporter
import org.junit.Test

class GenericExporterTest {
    
  @Test
  def Export() {
    val exporter = new GenericExporter();
    exporter.setTargetFolder(new java.io.File("target/gen1"));
    exporter.setSerializerClass(classOf[ScalaEntitySerializer]);
    exporter.setCreateScalaSources(true)
    exporter.export(getClass.getPackage);
  }

}

@QuerySupertype
class Superclass {
    
  var id: Int = _
    
}

@QueryEntity
class EntityClass extends Superclass {
    
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

