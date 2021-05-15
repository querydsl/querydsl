package com.querydsl.codegen.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

import org.junit.Test;

public class SurefireBooterTest {
    
    @Test
    public void test() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader instanceof URLClassLoader) {
            URLClassLoader cl = (URLClassLoader) classLoader;
            if (cl.getURLs().length == 1 && cl.getURLs()[0].getPath().contains("surefirebooter")) {
                URL url = cl.findResource("META-INF/MANIFEST.MF");
                Manifest manifest = new Manifest(url.openStream());
                System.out.println(manifest.getMainAttributes().getValue("Class-Path"));
            }
        }
    }

}
