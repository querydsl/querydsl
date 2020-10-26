/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * {@code ClassPathUtils} provides classpath scanning functionality
 *
 * @author tiwe
 */
public final class ClassPathUtils {

    /**
     * Return the classes from the given package and subpackages using the supplied classloader
     *
     * @param classLoader classloader to be used
     * @param pkg package to scan
     * @return set of found classes
     * @throws IOException
     */
    public static Set<Class<?>> scanPackage(ClassLoader classLoader, Package pkg) throws IOException {
        return scanPackage(classLoader, pkg.getName());
    }

    /**
     * Return the classes from the given package and subpackages using the supplied classloader
     *
     * @param classLoader classloader to be used
     * @param pkg package to scan
     * @return set of found classes
     * @throws IOException
     */
    public static Set<Class<?>> scanPackage(ClassLoader classLoader, String pkg) throws IOException {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(pkg, classLoader))
                .addClassLoader(classLoader)
                .filterInputsBy(new FilterBuilder().includePackage(pkg))
                .setScanners(new SubTypesScanner(false)));
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String typeNames : reflections.getStore().get(SubTypesScanner.class.getSimpleName()).values()) {
            Class<?> clazz = safeClassForName(classLoader, typeNames);
            if (clazz != null) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    /**
     * Get the class for the given className via the given classLoader
     *
     * @param classLoader classloader to be used
     * @param className fully qualified class name
     * @return {@code Class} instance matching the class name or null if not found
     */
    public static Class<?> safeClassForName(ClassLoader classLoader, String className) {
        try {
            if (className.startsWith("com.sun.") || className.startsWith("com.apple.")) {
                return null;
            } else {
                return Class.forName(className, true, classLoader);
            }
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return null;
        }
    }

    private ClassPathUtils() { }
}
