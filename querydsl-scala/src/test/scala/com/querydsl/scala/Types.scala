package com.querydsl.scala;

import com.querydsl.core.annotations._

@QuerySupertype
class Superclass {

  var id: Int = _

}

@QueryEntity
class EscapedWords {
  
  var `object`: String = _  
    
  var `type`: String = _
      
  var `var`: String = _
    
  var `val`: String = _
    
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