/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author tiwe
 *
 */
public class DirectoryWalkIterator implements Iterator<File> {

    private final Deque<File> files = new ArrayDeque<File>();
    
    public DirectoryWalkIterator(File directory) {
        File[] children = directory.listFiles();
        if (children != null){
            for (File file : children){
                files.add(file);
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !files.isEmpty();
    }

    @Override
    public File next() {
        if (!files.isEmpty()){
            File file = files.pop();
            File[] children = file.listFiles();
            if (children != null){
                for (File child : children){
                    files.add(child);
                }
            }
            return file;
        }else{
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
