/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * APUtils provides utilities for APT code generation in Querydsl
 * 
 * @author tiwe
 * @version $Id$
 */
public class APTUtils {

    public static Writer writerFor(File file) {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            System.err.println("Folder " + file.getParent()
                    + " could not be created");
        }
        try {
            return new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getFileContent(Map<String, String> options,
            String key, String defaultValue) throws IOException {
        String path = getString(options, key, null);
        if (path != null) {
            return FileUtils.readFileToString(new File(path), "UTF-8");
        } else {
            return "";
        }
    }

    public static String getString(Map<String, String> options, String key,
            String defaultValue) {
        String prefix = "-A" + key + "=";
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                return entry.getKey().substring(prefix.length());
            } else if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return defaultValue;
    }

}
