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
package com.querydsl.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.jdo.annotations.PersistenceCapable;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.mysema.codegen.CodeWriter;
import com.querydsl.apt.jdo.JDOAnnotationProcessor;
import com.querydsl.codegen.CodegenModule;
import com.querydsl.core.types.Expression;

public class PackageVerification {
    
    @Test
    public void Verify_Package() throws Exception{
        String version = System.getProperty("version");
        verify(new File("target/querydsl-jdo-"+version+"-apt-one-jar.jar"));        
    }

    private void verify(File oneJar) throws Exception {
        assertTrue(oneJar.getPath() + " doesn't exist", oneJar.exists());
        // verify classLoader
        URLClassLoader oneJarClassLoader = new URLClassLoader(new URL[]{oneJar.toURI().toURL()});
        oneJarClassLoader.loadClass(Expression.class.getName()); // querydsl-core
        oneJarClassLoader.loadClass(CodeWriter.class.getName()); // codegen
        oneJarClassLoader.loadClass(CodegenModule.class.getName()).newInstance();
        oneJarClassLoader.loadClass(PersistenceCapable.class.getName()); // jdo        
        Class cl = oneJarClassLoader.loadClass(JDOAnnotationProcessor.class.getName()); // querydsl-apt
        cl.newInstance();
        String resourceKey = "META-INF/services/javax.annotation.processing.Processor";
        assertEquals(JDOAnnotationProcessor.class.getName(), 
                Resources.toString(oneJarClassLoader.findResource(resourceKey), Charsets.UTF_8));
    }
    
}
