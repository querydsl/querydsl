package com.mysema.query.scala

import javax.persistence._
import com.mysema.query.codegen.GenericExporter
import org.junit.Test
import io.Source.fromFile

class GenericExporterJPATest {

  @Test
  def Export {
    val exporter = new GenericExporter()
    exporter.setTargetFolder(new java.io.File("target/gen1-jpa"))
    exporter.setSerializerClass(classOf[ScalaEntitySerializer])
    exporter.setTypeMappingsClass(classOf[ScalaTypeMappings])
    exporter.setEmbeddableAnnotation(classOf[Embeddable])
    exporter.setEmbeddedAnnotation(classOf[Embedded])
    exporter.setEntityAnnotation(classOf[Entity])
    exporter.setSkipAnnotation(classOf[Transient])
    exporter.setSupertypeAnnotation(classOf[MappedSuperclass])
    exporter.setCreateScalaSources(true)
    exporter.export(getClass.getPackage)

    val targetFolder = new java.io.File("target/gen1-jpa/com/mysema/query/scala/")
    CompileTestUtils.assertCompileSuccess(targetFolder)
  }

}

@MappedSuperclass
class JPASuperclass {

  var id: Int = _

}

@Entity
class JPAEscapedWords {
  
  var `object`: String = _  
    
  var `type`: String = _
      
  var `var`: String = _
    
  var `val`: String = _
    
}

@Entity
class JPAEntityClass extends JPASuperclass {

  // FIXME
  //  var comparable: Comparable[_] = _   

  var code: String = _

  var stringList: java.util.List[String] = _

  var embedded: JPAEmbeddableClass = _

}

@Entity
class JPAEntitySubclass extends JPAEntityClass {

  var property: String = _

}

@Embeddable
class JPAEmbeddableClass {

  var property: String = _

}

