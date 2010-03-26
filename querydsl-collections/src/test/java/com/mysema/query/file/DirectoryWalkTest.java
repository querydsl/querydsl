/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import java.io.File;

import org.junit.Test;

public class DirectoryWalkTest {
    
    @Test
    public void test(){
        DirectoryWalk walk = new DirectoryWalk(new File("target"));
        for (File file : walk){
            System.out.println(file.getPath());
        }
    }

}
