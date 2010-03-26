/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import java.io.File;
import java.util.Iterator;

/**
 * @author tiwe
 *
 */
public class DirectoryWalk implements Iterable<File>{

    private final File directory;
    
    public DirectoryWalk(String path) {
        this.directory = new File(path);
    }
    
    public DirectoryWalk(File directory) {
        this.directory = directory;
    }
    
    @Override
    public Iterator<File> iterator() {
        return new DirectoryWalkIterator(directory);
    }

}
