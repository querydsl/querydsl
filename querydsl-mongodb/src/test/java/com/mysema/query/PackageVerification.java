package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.google.code.morphia.annotations.Entity;
import com.mysema.codegen.CodeWriter;
import com.mysema.query.mongodb.MongodbAnnotationProcessor;
import com.mysema.query.types.Expression;

public class PackageVerification {
    
    @Test
    public void Verify_Package() throws ClassNotFoundException, IOException{
        String version = System.getProperty("version");
        verify(new File("target/querydsl-mongodb-"+version+"-apt-one-jar.jar"));        
    }

    private void verify(File oneJar) throws ClassNotFoundException, IOException {
        assertTrue(oneJar.getPath() + " doesn't exist", oneJar.exists());
        // verify classLoader
        URLClassLoader oneJarClassLoader = new URLClassLoader(new URL[]{oneJar.toURI().toURL()});
        oneJarClassLoader.loadClass(Expression.class.getName()); // querydsl-core
        oneJarClassLoader.loadClass(CodeWriter.class.getName()); // codegen
        oneJarClassLoader.loadClass(Entity.class.getName()); // morphia        
        oneJarClassLoader.loadClass(MongodbAnnotationProcessor.class.getName()); // querydsl-apt
        String resourceKey = "META-INF/services/javax.annotation.processing.Processor";
        assertEquals(MongodbAnnotationProcessor.class.getName(), IOUtils.toString(oneJarClassLoader.findResource(resourceKey).openStream()));
    }
    
}
