/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tiwe
 *
 */
public class Pattern {

    private StringBuffer str = new StringBuffer();
    
    private final List<Element> elements = new ArrayList<Element>();
    
    public List<Element> getElements(){
        return elements;
    }
    
    public Pattern arg(int index){
        str.append("{").append(index).append("}");
        elements.add(new ArgElement(index));
        return this;
    }
    
    public Pattern text(String text){
        str.append(text);
        elements.add(new TextElement(text));
        return this;
    }
    
    public interface Element { }
    
    public static class ArgElement implements Element {     
        private final int index;        
        private ArgElement (int index){
            this.index = index;
        }        
        public int getIndex(){
            return index;
        }
    }
    
    public static class TextElement implements Element {
        private String text;
        private TextElement(String text) {
            this.text = text;
        }
        public String getText() {
            return text;
        }
    }
    
    public String toString(){
        return str.toString();
    }
    
}
