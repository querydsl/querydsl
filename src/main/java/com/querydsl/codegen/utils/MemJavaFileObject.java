/*
 * Copyright 2010, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.codegen.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.tools.SimpleJavaFileObject;

/**
 * MemJavaFileObject defines an in memory compiled Java file
 * 
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
        if (baos == null) {
            throw new FileNotFoundException(name);
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    @Override
    public String getName() {
        return name;
    }

    public byte[] getByteArray() {
        return baos.toByteArray();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        if (baos == null) {
            throw new FileNotFoundException(name);
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        if (baos == null) {
            baos = new ByteArrayOutputStream();
        }
        return baos;
    }

}
