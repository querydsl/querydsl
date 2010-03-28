/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mysema.query.QueryException;

public final class QFileUtils {
    
    private QFileUtils(){}
    
    public static String readFileToString(File file, String enc){
        try {
            return FileUtils.readFileToString(file, enc);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

}
