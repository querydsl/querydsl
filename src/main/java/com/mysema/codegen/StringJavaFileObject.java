/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.SimpleJavaFileObject;

/**
 * @author tiwe
 *
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    
    private final String contents;

    public StringJavaFileObject(String className, String contents) throws URISyntaxException{
        super(new URI(className), Kind.SOURCE);
        this.contents = contents;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return contents;
    }
    
    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return kind.equals(getKind()) && 
            (simpleName.equals(toUri().getPath()) || toUri().getPath().endsWith("/" + simpleName));
    }
    
}
