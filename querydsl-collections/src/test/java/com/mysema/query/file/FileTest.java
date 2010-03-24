package com.mysema.query.file;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;

public class FileTest {

    @Test
    public void path_to_file_map(){
        Map<String,File> files = MiniApi
            .from(QFile.any, new File(".").listFiles())
            .map(QFile.any.absolutePath, QFile.any);
        
        for (Map.Entry<String, File> entry : files.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
