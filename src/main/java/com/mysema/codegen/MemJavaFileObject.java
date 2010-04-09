/*
 * Copyright (c) 2010 Mysema Ltd.
 * 
 * base on code from https://hickory.dev.java.net/
 * 
 */

package com.mysema.codegen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * @author tiwe
 *
 */
public class MemJavaFileObject extends SimpleJavaFileObject {
    
    private ByteArrayOutputStream baos;
    
    private final String name;
    
    public MemJavaFileObject(String urlPrefix, String name, Kind kind) {
        super(URI.create(urlPrefix + name + kind.extension), kind);
        this.name = name;
    }
    
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        if(baos == null){
            throw new FileNotFoundException(name);
        }
        return new String(baos.toByteArray());
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public byte[] getByteArray(){
        return baos.toByteArray();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        if(baos == null){
            throw new FileNotFoundException(name);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }
    
    @Override
    public OutputStream openOutputStream() throws IOException {
        if (baos == null){
            baos = new ByteArrayOutputStream();
        }
        return baos;
    }
    
}
