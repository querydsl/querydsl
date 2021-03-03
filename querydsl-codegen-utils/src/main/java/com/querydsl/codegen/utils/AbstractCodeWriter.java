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

/**
 * @author tiwe
 * 
 * @param <T>
 */
public abstract class AbstractCodeWriter<T extends AbstractCodeWriter<T>> implements Appendable,
        CodeWriter {

    private final Appendable appendable;

    private final int spaces;

    private final String spacesString;

    private String indent = "";

    @SuppressWarnings("unchecked")
    private final T self = (T) this;

    public AbstractCodeWriter(Appendable appendable, int spaces) {
        if (appendable == null) {
            throw new IllegalArgumentException("appendable is null");
        }
        this.appendable = appendable;
        this.spaces = spaces;
        this.spacesString = StringUtils.repeat(' ', spaces);
    }

    @Override
    public T append(char c) throws IOException {
        appendable.append(c);
        return self;
    }

    @Override
    public T append(CharSequence csq) throws IOException {
        appendable.append(csq);
        return self;
    }

    @Override
    public T append(CharSequence csq, int start, int end) throws IOException {
        appendable.append(csq, start, end);
        return self;
    }

    @Override
    public T beginLine(String... segments) throws IOException {
        append(indent);
        for (String segment : segments) {
            append(segment);
        }
        return self;
    }

    protected T goIn() {
        indent += spacesString;
        return self;
    }

    protected T goOut() {
        if (indent.length() >= spaces) {
            indent = indent.substring(0, indent.length() - spaces);
        }
        return self;
    }

    @Override
    public T line(String... segments) throws IOException {
        append(indent);
        for (String segment : segments) {
            append(segment);
        }
        return nl();
    }

    @Override
    public T nl() throws IOException {
        return append("\n");
    }

}
