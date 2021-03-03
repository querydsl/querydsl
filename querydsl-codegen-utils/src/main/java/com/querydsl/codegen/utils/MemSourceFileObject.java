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

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * MemSourceFileObject defines a in-memory Java source file object
 * 
 * @author tiwe
 * 
 */
public class MemSourceFileObject extends SimpleJavaFileObject {

    private static URI toUri(String fqname) {
        return URI.create("file:///" + fqname.replace(".", "/") + ".java");
    }

    private final StringBuilder contents;

    public MemSourceFileObject(String fullName) {
        super(toUri(fullName), JavaFileObject.Kind.SOURCE);
        contents = new StringBuilder(1000);
    }

    public MemSourceFileObject(String fullName, String content) {
        this(fullName);
        contents.append(content);
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return contents;
    }

    @Override
    public Writer openWriter() {
        return new Writer() {
            @Override
            public Writer append(CharSequence csq) throws IOException {
                contents.append(csq);
                return this;
            }

            @Override
            public void close() {
            }

            @Override
            public void flush() {
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                contents.append(cbuf, off, len);
            }
        };
    }
}
