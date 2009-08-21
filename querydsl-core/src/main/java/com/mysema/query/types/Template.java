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

/**
 * Template for operation and path serialization
 * 
 * @author tiwe
 * 
 */
@Immutable
public final class Template {

    // s -> toString()
    // l -> lower()
    // u -> upper()
    
    @Immutable
    public static final class Element {
        
        private final int index;
        
        @Nullable
        private final String staticText;
        
        private final boolean asString;

        private Element(int index) {
            this(index, false);
        }

        private Element(int index, boolean asString) {
            this.index = index;
            this.staticText = null;
            this.asString = asString;
        }

        private Element(String text) {
            this.index = -1;
            this.staticText = text;
            this.asString = true;
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

    private static final Pattern elementPattern = Pattern.compile("\\{\\d+s?\\}");

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
            String index = template.substring(m.start() + 1, m.end() - 1);
            if (index.endsWith("s")) {
                elements.add(new Element(Integer.parseInt(index.substring(0, index.length() - 1)), true));
            } else {
                elements.add(new Element(Integer.parseInt(index)));
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
