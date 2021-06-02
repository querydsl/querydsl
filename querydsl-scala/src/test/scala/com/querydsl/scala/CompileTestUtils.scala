package com.querydsl.scala

import java.io.{File, FileOutputStream, OutputStreamWriter}
import java.io.File.pathSeparator
import java.nio.charset.StandardCharsets
import io.github.classgraph.ClassGraph
import scala.tools.nsc._
import scala.tools.nsc.reporters.ConsoleReporter
import collection.JavaConverters._

object CompileTestUtils {

  private def jarPathOfClass(className: String) = {
    Class.forName(className).getProtectionDomain.getCodeSource.getLocation
  }

  private val currentLibraries = new ClassGraph().getClasspathURLs.asScala.toList
  private val cp = jarPathOfClass("scala.tools.nsc.Interpreter") :: jarPathOfClass("scala.AnyVal") :: currentLibraries

  private val env = new Settings()
  env.classpath.value = cp.mkString(pathSeparator)
  env.usejavacp.value = true
  //env.d.value = "target"
  env.stopAfter.value = List("refchecks")

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
    val file = File.createTempFile("source", ".scala")
    try {
      val writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      writer.write(source);
      writer.flush()
      writer.close();
      assertCompileSuccess(file)
    } finally {
        file.delete()
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
