/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.expr.Expr;

/**
 * Template for operation and path serialization
 * 
 * @author tiwe
 * 
 */
@Immutable
public final class Template {
        
    @Immutable
    public static final class Element {
        
        private final int index;
        
        @Nullable
        private final String staticText;
        
        @Nullable
        private final Converter<?,?> converter;
        
        private final boolean asString;

        private Element(int index) {
            this(index, false, null, null);
        }

        private Element(int index, Converter<?,?> converter) {
            this(index, false, null, converter);
        }
        
        private Element(int index, boolean asString) {
            this(index, asString, null, null);
        }

        private Element(String text) {
            this(-1, false, text, null);
        }

        private Element(int index, boolean asString, @Nullable String text, @Nullable Converter<?,?> converter){
            this.index = index;
            this.asString = asString;
            this.staticText = text;
            this.converter = converter;
        }
        
        public int getIndex() {
            return index;
        }

        @Nullable
        public String getStaticText() {
            return staticText;
        }

        public boolean isAsString() {
            return asString;
        }
        
        public boolean hasConverter(){
            return converter != null;
        }
        
        @SuppressWarnings("unchecked")
        public Expr<?> convert(Expr<?> source){
            return ((Converter)converter).convert(source);
        }

        @Override
        public String toString() {
            if (staticText != null) {
                return "'" + staticText + "'";
            } else if (asString){
                return index + "s";
            }else {
                return String.valueOf(index);
            }
        }
    }

    private static final Pattern elementPattern = Pattern.compile("\\{\\d+[slu]?\\}");

    private final List<Element> elements = new ArrayList<Element>();

    private final String template;

    public Template(String template) {
        this.template = template;
        Matcher m = elementPattern.matcher(template);
        int end = 0;
        while (m.find()) {
            if (m.start() > end) {
                elements.add(new Element(template.substring(end, m.start())));
            }
            String str = template.substring(m.start() + 1, m.end() - 1).toLowerCase();
            boolean asString = false;
            Converter<?,?> converter = null;
            switch (str.charAt(str.length()-1)){
              case 'l' : converter = Converters.toLowerCase; break;
              case 'u' : converter = Converters.toUpperCase; break;
              case 's' : asString = true; break;
            }
            if (asString || converter != null){
                str = str.substring(0, str.length()-1);
            }
            int index = Integer.parseInt(str);
            if (asString){
                elements.add(new Element(index, true));
            }else if (converter != null){
                elements.add(new Element(index, converter));
            }else{
                elements.add(new Element(index));
            }
            end = m.end();
        }
        if (end < template.length()) {
            elements.add(new Element(template.substring(end)));
        }
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return template;
    }
    
}
