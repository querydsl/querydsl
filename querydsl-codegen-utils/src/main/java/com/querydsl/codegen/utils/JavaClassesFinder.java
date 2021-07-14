/*
 * Copyright 2018, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen.utils;

import static javax.tools.JavaFileObject.Kind.CLASS;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import javax.tools.JavaFileObject;

/**
 * JavaClassesFinder finds Java file classes using the specified Classloader
 *
 * NOTE: This is a derivative work from an article of atamur. Original article can be found here:
 * http://atamur.blogspot.com/2009/10/using-built-in-javacompiler-with-custom.html
 *
 * @author matteo-gallo-bb
 */
class JavaClassesFinder {

  private ClassLoader classLoader;

  JavaClassesFinder(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  /**
   * List all the Java file classes that are present in the specified package.
   *
   * @return a list of Java file classes
   */
  public List<JavaFileObject> listAll(String packageName) throws IOException {
    String javaPackageName = packageName.replaceAll("\\.", "/");

    List<JavaFileObject> result = new ArrayList<JavaFileObject>();

    Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
    while (urlEnumeration.hasMoreElements()) {
      // one URL for each jar on the classpath that has the given package
      URL packageFolderURL = urlEnumeration.nextElement();
      result.addAll(listUnder(packageName, packageFolderURL));
    }

    return result;
  }

  private Collection<JavaFileObject> listUnder(String packageName, URL packageFolderURL) {
    File directory = new File(packageFolderURL.getFile());
    if (directory.isDirectory()) {
      // browse local .class files - useful for local execution
      return processDir(packageName, directory);
    } else {
      // browse a jar file
      return processJar(packageFolderURL);
    }
  }

  private List<JavaFileObject> processJar(URL packageFolderURL) {
    List<JavaFileObject> result = new ArrayList<JavaFileObject>();
    try {
      JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
      String jarUri = jarConn.getJarFileURL().toString();
      String rootEntryName = jarConn.getEntryName() != null ? jarConn.getEntryName() : "";
      int rootEnd = rootEntryName.length() + 1;

      Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
      while (entryEnum.hasMoreElements()) {
        JarEntry jarEntry = entryEnum.nextElement();
        String name = jarEntry.getName();
        if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS.extension)) {
          URI uri = URI.create(jarUri + "!/" + name);
          String binaryName = name.replaceAll("/", ".");
          binaryName = binaryName.replaceAll(CLASS.extension + "$", "");

          result.add(new CompiledJavaFileObject(binaryName, uri));
        }
      }
    } catch (Exception e) {
      throw new CodegenException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
    }
    return result;
  }

  private List<JavaFileObject> processDir(String packageName, File directory) {
    List<JavaFileObject> result = new ArrayList<JavaFileObject>();

    File[] childFiles = directory.listFiles();
    for (File childFile : childFiles) {
      // We only want the .class files
      if (childFile.isFile() && childFile.getName().endsWith(CLASS.extension)) {
        String binaryName = packageName + "." + childFile.getName();
        binaryName = binaryName.replaceAll(CLASS.extension + "$", "");

        result.add(new CompiledJavaFileObject(binaryName, childFile.toURI()));
      }
    }

    return result;
  }
}
