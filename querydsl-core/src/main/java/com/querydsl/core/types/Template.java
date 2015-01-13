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
package com.querydsl.core.types;

import java.io.Serializable;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

/**
 * Template for {@link Operation}, {@link TemplateExpression} and {@link Path} serialization
 *
 * @author tiwe
 *
 */
@Immutable
public final class Template implements Serializable {

    private static final long serialVersionUID = -1697705745769542204L;

    @Immutable
    public abstract static class Element implements Serializable {

        private static final long serialVersionUID = 3396877288101929387L;

        public abstract Object convert(List<?> args);

        public abstract boolean isString();

    }

    public static final class AsString extends Element {

        private static final long serialVersionUID = -655362047873616197L;

        private final int index;

        private final String toString;

        public AsString(int index) {
            this.index = index;
            this.toString = index + "s";
        }

        @Override
        public Object convert(final List<?> args) {
            final Object arg = args.get(index);
            return arg instanceof Constant ? arg.toString() : arg;
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    public static final class StaticText extends Element {

        private static final long serialVersionUID = -2791869625053368023L;

        private final String text;

        private final String toString;

        public StaticText(String text) {
            this.text = text;
            this.toString = "'" + text + "'";
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public Object convert(List<?> args) {
            return text;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    public static final class Transformed extends Element {

        private static final long serialVersionUID = 702677732175745567L;

        private final int index;

        private final transient Function<Object, Object> transformer;

        private final String toString;

        public Transformed(int index, Function<Object, Object> transformer) {
            this.index = index;
            this.transformer = transformer;
            this.toString = String.valueOf(index);
        }

        @Override
        public Object convert(final List<?> args) {
            return transformer.apply(args.get(index));
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    public static final class ByIndex extends Element {

        private static final long serialVersionUID = 4711323946026029998L;

        private final int index;

        private final String toString;

        public ByIndex(int index) {
            this.index = index;
            this.toString = String.valueOf(index);
        }

        @Override
        public Object convert(final List<?> args) {
            final Object arg = args.get(index);
            if (arg instanceof Expression) {
                return ExpressionUtils.extract((Expression<?>) arg);
            } else {
                return arg;
            }
        }

        public int getIndex() {
            return index;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String toString() {
            return toString;
        }

    }

    private final ImmutableList<Element> elements;

    private final String template;

    Template(String template, ImmutableList<Element> elements) {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Template) {
            return ((Template)o).template.equals(template);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return template.hashCode();
    }

}
