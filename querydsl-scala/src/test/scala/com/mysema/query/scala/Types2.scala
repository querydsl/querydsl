package com.mysema.query.scala;

import com.mysema.query.annotations._
import scala.runtime.RichChar

package ext {

  object WeekDay extends Enumeration {
    type WeekDay = Value
    val Mon, Tue, Wed, Thu, Fri, Sat, Sun = Value
  }
  
  @QueryEntity
  class SomeEntity extends SomeTrait {
    var someStringOption: Option[String] = _

    var someScalaEnum: WeekDay.Value = _

    var someChar: Char = _
    
    var someRichChar: RichChar = _
  }

  trait SomeTrait {
    private var somePrivateString: String = _
  
  }

}