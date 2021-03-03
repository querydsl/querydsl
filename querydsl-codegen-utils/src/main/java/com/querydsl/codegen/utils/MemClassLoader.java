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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

/**
 * MemClassLoader is a mmemory based implementation of the ClassLoader interface
 * 
 * @author tiwe
 * 
 */
public final class MemClassLoader extends ClassLoader {

    private static final LocationAndKind CLASS_KEY = new LocationAndKind(
            StandardLocation.CLASS_OUTPUT, Kind.CLASS);

    private static final LocationAndKind OTHER_KEY = new LocationAndKind(
            StandardLocation.CLASS_OUTPUT, Kind.OTHER);

    private static final LocationAndKind SOURCE_KEY = new LocationAndKind(
            StandardLocation.CLASS_OUTPUT, Kind.SOURCE);

    private final Map<LocationAndKind, Map<String, JavaFileObject>> memFileSystem;

    public MemClassLoader(ClassLoader parent,
            Map<LocationAndKind, Map<String, JavaFileObject>> ramFileSystem) {
        super(parent);
        this.memFileSystem = ramFileSystem;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaFileObject jfo = memFileSystem.get(CLASS_KEY).get(name);
        if (jfo != null) {
            byte[] bytes = ((MemJavaFileObject) jfo).getByteArray();
            return defineClass(name, bytes, 0, bytes.length);
        } else {
            return super.findClass(name);
        }
    }

    @Override
    protected URL findResource(String name) {
        URL retValue = super.findResource(name);
        if (retValue != null) {
            return retValue;
        } else {
            JavaFileObject jfo = getFileObject(name);
            if (jfo != null) {
                try {
                    return jfo.toUri().toURL();
                } catch (MalformedURLException ex) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private JavaFileObject getFileObject(String n) {
        LocationAndKind key;
        String name;
        if (n.endsWith(Kind.CLASS.extension)) {
            name = n.replace('.', '/') + Kind.CLASS.extension;
            key = CLASS_KEY;
        } else if (n.endsWith(Kind.SOURCE.extension)) {
            name = n.replace('.', '/') + Kind.SOURCE.extension;
            key = SOURCE_KEY;
        } else {
            name = n;
            key = OTHER_KEY;
        }
        if (memFileSystem.containsKey(key)) {
            return memFileSystem.get(key).get(name);
        } else {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        JavaFileObject jfo = getFileObject(name);
        if (jfo != null) {
            byte[] bytes = ((MemJavaFileObject) jfo).getByteArray();
            return new ByteArrayInputStream(bytes);
        } else {
            return null;
        }
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> retValue;
        retValue = Collections.list(super.getResources(name));
        JavaFileObject jfo = getFileObject(name);
        if (jfo != null) {
            retValue.add(jfo.toUri().toURL());
        }
        return Collections.enumeration(retValue);
    }
}
