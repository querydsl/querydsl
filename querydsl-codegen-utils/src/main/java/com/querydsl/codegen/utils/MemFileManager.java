/*
 * Copyright 2010, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

/**
 * MemFileManager is a memory based implementation of the JavaFileManager interface
 *
 * @author tiwe
 */
public class MemFileManager extends ForwardingJavaFileManager<JavaFileManager> {

  private final ClassLoader classLoader;

  private final Map<LocationAndKind, Map<String, JavaFileObject>> ramFileSystem;

  private final String urlPrefix;

  private final JavaClassesFinder finder;

  public MemFileManager(ClassLoader parent, StandardJavaFileManager sjfm) {
    super(sjfm);
    ramFileSystem = new HashMap<LocationAndKind, Map<String, JavaFileObject>>();
    Map<String, JavaFileObject> classLoaderContent = new HashMap<String, JavaFileObject>();
    ramFileSystem.put(new LocationAndKind(StandardLocation.CLASS_OUTPUT, Kind.CLASS),
        classLoaderContent);
    classLoader = new MemClassLoader(parent, ramFileSystem);
    urlPrefix = MemFileSystemRegistry.DEFAULT.getUrlPrefix(this);
    finder = new JavaClassesFinder(classLoader);
  }

  @Override
  public ClassLoader getClassLoader(JavaFileManager.Location location) {
    return classLoader;
  }

  @Override
  public FileObject getFileForInput(JavaFileManager.Location location, String packageName,
      String relativeName) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public FileObject getFileForOutput(JavaFileManager.Location location, String packageName,
      String relativeName, FileObject sibling) throws IOException {
    String name = null;
    if ("".equals(packageName)) {
      name = relativeName;
    } else {
      name = packageName.replace('.', '/') + "/" + relativeName;
    }
    LocationAndKind key = new LocationAndKind(location, Kind.OTHER);
    if (ramFileSystem.containsKey(key)) {
      JavaFileObject jfo = ramFileSystem.get(key).get(name);
      if (jfo != null) {
        return jfo;
      }
    }
    JavaFileObject jfo = new MemJavaFileObject(urlPrefix, name, Kind.OTHER);
    register(key, jfo);
    return jfo;
  }

  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String name, Kind kind,
      FileObject sibling) throws IOException {
    JavaFileObject javaFileObject = null;
    LocationAndKind key = new LocationAndKind(location, kind);

    if (ramFileSystem.containsKey(key)) {
      javaFileObject = ramFileSystem.get(key).get(name);
      if (javaFileObject != null) {
        return javaFileObject;
      }
    }
    if (kind == Kind.SOURCE) {
      javaFileObject = new MemSourceFileObject(name);
    } else {
      javaFileObject = new MemJavaFileObject(urlPrefix, name, kind);
    }
    register(key, javaFileObject);
    return javaFileObject;
  }

  @Override
  public String inferBinaryName(Location loc, JavaFileObject javaFileObject) {
    String result;
    if (loc == StandardLocation.CLASS_PATH && javaFileObject instanceof MemJavaFileObject) {
      result = javaFileObject.getName();
    } else if (javaFileObject instanceof CompiledJavaFileObject) {
      result = ((CompiledJavaFileObject) javaFileObject).binaryName();
    } else {
      result = super.inferBinaryName(loc, javaFileObject);
    }
    return result;
  }

  @Override
  public boolean isSameFile(FileObject a, FileObject b) {
    return a.equals(b);
  }

  @Override
  public Iterable<JavaFileObject> list(Location location, String pkg, Set<Kind> kinds,
      boolean recurse) throws IOException {

    List<JavaFileObject> result = new ArrayList<JavaFileObject>();
    if (location == StandardLocation.PLATFORM_CLASS_PATH) {
      // let standard manager handle
      return super.list(location, pkg, kinds, recurse);
    } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
      for (JavaFileObject f : super.list(location, pkg, kinds, recurse)) {
        result.add(f);
      }
      // app specific classes are here
      result.addAll(finder.listAll(pkg));
    }
    // here we add to the results the classes that Codegen has generated and has put in memory
    result.addAll(addInMemoryClasses(location, pkg, kinds, recurse));

    return result;
  }

  private List<JavaFileObject> addInMemoryClasses(Location location, String pkg, Set<Kind> kinds, boolean recurse) {
    List<JavaFileObject> result = new ArrayList<JavaFileObject>();

    if (location == StandardLocation.CLASS_PATH) {
      location = StandardLocation.CLASS_OUTPUT;
    }

    for (Kind kind : kinds) {
      LocationAndKind key = new LocationAndKind(location, kind);
      if (ramFileSystem.containsKey(key)) {
        Map<String, JavaFileObject> locatedFiles = ramFileSystem.get(key);
        for (Map.Entry<String, JavaFileObject> entry : locatedFiles.entrySet()) {
          JavaFileObject processedFile = processLocatedFile(pkg, kinds, recurse, entry);
          if (processedFile != null) {
            result.add(processedFile);
          }
        }
      }
    }
    return result;
  }

  private void register(LocationAndKind key, JavaFileObject javaFileObject) {
    if (!ramFileSystem.containsKey(key)) {
      ramFileSystem.put(key, new HashMap<String, JavaFileObject>());
    }
    ramFileSystem.get(key).put(javaFileObject.getName(), javaFileObject);
  }

  private JavaFileObject processLocatedFile(String pkg, Set<Kind> kinds, boolean recurse, Map.Entry<String, JavaFileObject> entry) {
    String name = entry.getKey();
    String packageName = "";
    if (name.indexOf('.') > -1) {
      packageName = name.substring(0, name.lastIndexOf('.'));
    }
    if (recurse ? packageName.startsWith(pkg) : packageName.equals(pkg)) {
      JavaFileObject candidate = entry.getValue();
      if (kinds.contains(candidate.getKind())) {
        return candidate;
      }
    }
    return null;
  }

}
