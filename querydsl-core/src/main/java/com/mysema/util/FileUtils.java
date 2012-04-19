package com.mysema.util;

import java.io.File;
import java.io.IOException;

public abstract class FileUtils {

    public static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
    }
    
    private FileUtils() {}

}
