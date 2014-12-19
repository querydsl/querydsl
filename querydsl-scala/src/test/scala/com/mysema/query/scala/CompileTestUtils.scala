package com.mysema.query.scala

import java.io.File._

import scala.tools.nsc._
import scala.tools.nsc.interpreter.IR.Success
import scala.io.Source.fromFile
import java.io.File

trait CompileTestUtils {
    
  private def jarPathOfClass(className: String) = {
    Class.forName(className).getProtectionDomain.getCodeSource.getLocation
  }

  def assertCompileSuccess(files: Traversable[File]): Unit = {
    assertCompileSuccess(files
                           map (fromFile(_).mkString)
                           mkString ("\n"))
  }

  private lazy val env = {
    import java.io.File.pathSeparator
    val env = new Settings

    val currentLibraries = (this.getClass.getClassLoader).asInstanceOf[java.net.URLClassLoader].getURLs().toList
    val cp = jarPathOfClass("scala.tools.nsc.Interpreter") :: jarPathOfClass("scala.ScalaObject") :: currentLibraries

    env.classpath.value = cp.mkString(pathSeparator)

    env.usejavacp.value = true
    env
  }

  def assertCompileSuccess(source: String): Unit = {
    val out = new java.io.ByteArrayOutputStream
    val interpreterWriter = new java.io.PrintWriter(out)

    val interpreter = new Interpreter(env, interpreterWriter)
    try {
      val result = interpreter.interpret(source.replaceAll("package", "import"))
      if (result != Success) {
        throw new AssertionError("Compile failed, interpreter output:\n" + out.toString("utf-8"))
      }
    } finally {
      interpreterWriter.close
      interpreter.close
    }
  }
  
  def recursiveFileList(file: File): Array[File] = {
    if (file.isDirectory) {
      file.listFiles.flatMap(recursiveFileList(_))
    } else {
      Array(file)
    }
  }  
}
