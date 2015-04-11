/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.codegen;

import org.reflections.Reflections;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * ClassPathUtils provides classpath scanning functionality
 *
 * @author tiwe
 */
public final class ClassPathUtils {

    public static Set<Class<?>> scanPackage(ClassLoader classLoader, Package pkg) throws IOException {
        return scanPackage(classLoader, pkg.getName());
    }

    public static Set<Class<?>> scanPackage(ClassLoader classLoader, String pkg) throws IOException {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(pkg, classLoader))
                .addClassLoader(classLoader)
                .setScanners(new SubTypesScanner(false))
                .setMetadataAdapter(new JavaReflectionAdapter()));
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String typeNames : reflections.getStore().get(SubTypesScanner.class.getSimpleName()).values()) {
            Class<?> clazz = safeClassForName(classLoader, typeNames);
            if (clazz != null) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    public static Class<?> safeClassForName(ClassLoader classLoader, String className) {
        try {
            if (className.startsWith("com.sun") || className.startsWith("com.apple")) {
                return null;
            } else {
                return Class.forName(className, true, classLoader);
            }
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    private ClassPathUtils() {}
}
