package com.mysema.query.scala

import scala.tools.nsc._
import scala.tools.nsc.InterpreterResults._

trait CompileTestUtils {
  private def jarPathOfClass(className: String) = {
    Class.forName(className).getProtectionDomain.getCodeSource.getLocation
  }

  def assertCompileSuccess(source: String): Unit = {
    import java.io.File.pathSeparator

    val env = new Settings

    val currentLibraries = (this.getClass.getClassLoader).asInstanceOf[java.net.URLClassLoader].getURLs().toList.mkString(pathSeparator)
    val cp = currentLibraries :: jarPathOfClass("scala.tools.nsc.Interpreter") :: jarPathOfClass("scala.ScalaObject") :: Nil

    env.classpath.value = cp.mkString(pathSeparator)

    env.usejavacp.value = true

    val out = new java.io.ByteArrayOutputStream
    val interpreterWriter = new java.io.PrintWriter(out)

    val interpreter = new Interpreter(env, interpreterWriter)
    try {
      val result = interpreter.interpret(source.replace("package", "import"))
      if (result != Success) {
        throw new AssertionError("Compile failed, interpreter output:\n" + out.toString("utf-8"))
      }
    } finally {
      interpreterWriter.close
      interpreter.close
    }
  }
}