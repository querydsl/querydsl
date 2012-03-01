/*
 * Copyright 2011, Mysema Ltd
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
package com.mysema.query.types;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.collections15.Transformer;

/**
 * Template for {@link Operation}, {@link TemplateExpression} and {@link Path} serialization
 *
 * @author tiwe
 *
 */
public final class Template implements Serializable{

    private static final long serialVersionUID = -1697705745769542204L;

    public static final class Element implements Serializable{

        private static final long serialVersionUID = -6861235060996903489L;

        private final int index;

        @Nullable
        private final String staticText;

        @Nullable
        private final transient Transformer<Expression<?>,Expression<?>> transformer;

        private final boolean asString;

        private final String toString;

        @SuppressWarnings("unchecked")
        Element(int index, Transformer<? extends Expression<?>,? extends Expression<?>> transformer) {
            this.asString = false;
            this.transformer = (Transformer)transformer;
            this.index = index;
            this.staticText = null;
            this.toString = String.valueOf(index);
        }

        Element(int index, boolean asString) {
            this.asString = asString;
            this.transformer = null;
            this.index = index;
            this.staticText = null;
            this.toString = index + (asString ? "s" : "");
        }

        Element(String text) {
            this.asString = false;
            this.transformer = null;
            this.index = -1;
            this.staticText = text;
            this.toString = "'" + staticText + "'";
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
            return transformer != null;
        }

        public Expression<?> convert(Expression<?> source){
            return transformer.transform(source);
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    private final List<Element> elements;

    private final String template;

    Template(String template, List<Element> elements) {
        this.template = template;
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return template;
    }
    
    public boolean equals(Object o){
        if (o == this) {
            return true;
        } else if (o instanceof Template) {
            return ((Template)o).template.equals(template);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return template.hashCode();
    }

}
