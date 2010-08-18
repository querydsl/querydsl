/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen;

import static com.mysema.codegen.Symbols.NEWLINE;
import static com.mysema.codegen.Symbols.SEMICOLON;

import java.io.IOException;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractCodeWriter<T extends AbstractCodeWriter<T>> implements Appendable, CodeWriter{
    
    private static final int INDENT_SPACES = 4;
    
    private final Appendable appendable;
    
    private String indent = "";

    @SuppressWarnings("unchecked")
    private final T self = (T)this;
        
    public AbstractCodeWriter(Appendable appendable){
        if (appendable == null){
            throw new IllegalArgumentException("appendable is null");
        }
        this.appendable = appendable;
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
        for (String segment : segments){
            append(segment);
        }
        return self;
    }
    
    protected T goIn(){
        indent += "    ";
        return self;
    }
    
    
    protected T goOut(){
        if (indent.length() >= INDENT_SPACES){
            indent = indent.substring(0, indent.length() - INDENT_SPACES);
        }
        return self;
    }
    
    @Override
    public T line(String... segments) throws IOException{
        append(indent);
        for (String segment : segments){
            append(segment);
        }        
        return nl();
    }
    
    @Override
    public T nl() throws IOException {
        return append(NEWLINE);        
    }

}
