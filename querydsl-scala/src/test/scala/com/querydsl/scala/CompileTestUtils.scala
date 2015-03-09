package com.querydsl.scala

import java.io.File._

import scala.tools.nsc._
import scala.tools.nsc.interpreter.IR.Success
import scala.io.Source.fromFile
import java.io.File
import java.io.File.pathSeparator

import scala.tools.nsc.reporters.{ConsoleReporter, Reporter}

object CompileTestUtils {

  private def jarPathOfClass(className: String) = {
    Class.forName(className).getProtectionDomain.getCodeSource.getLocation
  }

  private val env = new Settings()
  private val currentLibraries = this.getClass.getClassLoader.asInstanceOf[java.net.URLClassLoader].getURLs.toList
  private val cp = jarPathOfClass("scala.tools.nsc.Interpreter") :: jarPathOfClass("scala.ScalaObject") :: currentLibraries
  env.classpath.value = cp.mkString(pathSeparator)
  env.usejavacp.value = true
  env.d.value = "target"

  def assertCompileSuccess(file: File): Unit = {
    assertCompileSuccess(recursiveFileList(file))
  }

  def assertCompileSuccess(files: Traversable[File]): Unit = {
    val reporter = new ConsoleReporter(env)
    val g = new Global(env, reporter)
    val run = new g.Run
    run.compile(files.map(_.getPath).toList)
    if (reporter.hasErrors) {
      throw new AssertionError("Compilation failed")
    }
  }

  def assertCompileSuccess(source: String): Unit = {
    val out = new java.io.ByteArrayOutputStream
    val interpreterWriter = new java.io.PrintWriter(out)

    val interpreter = new scala.tools.nsc.interpreter.IMain(env, interpreterWriter)
    try {
      val result = interpreter.interpret(source.replaceAll("package [\\w\\.]+", ""))
      if (result != Success) {
        throw new AssertionError("Compile failed, interpreter output:\n" + out.toString("utf-8"))
      }
    } finally {
      interpreterWriter.close()
      interpreter.close()
    }
  }
  
  private def recursiveFileList(file: File): Array[File] = {
    if (file.isDirectory) {
      file.listFiles.flatMap(recursiveFileList)
    } else {
      Array(file)
    }
  }  
}
