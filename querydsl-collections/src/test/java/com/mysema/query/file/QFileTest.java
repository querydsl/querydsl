/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import java.io.File;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.collections.MiniApi;

public class QFileTest {

    @Test
    public void path_to_file_map(){
        QFile anyFile = QFile.any;
        Map<String,File> files = MiniApi
            .from(anyFile, new File(".").listFiles())
            .map(anyFile.absolutePath, anyFile);
        
        for (Map.Entry<String, File> entry : files.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
    
    @Test
    public void walk(){        
        QFile anyFile = QFile.any;
        for (File file : MiniApi
                .from(anyFile, QFile.walk(new File("target")))
                .where(anyFile.name.endsWith(".class"))
                .list(anyFile)){
            System.out.println(file.getName());
        }
    }
    
    @Test
    @Ignore
    public void getContent(){
        QFile anyFile = QFile.any;
        Map<File,String> rv = MiniApi
                .from(anyFile, QFile.walk(new File("src/test")))
                .where(anyFile.name.endsWith(".properties"))
                .map(anyFile, anyFile.getContent("ISO-8859-1"));
        System.out.println(rv);
    }
}
