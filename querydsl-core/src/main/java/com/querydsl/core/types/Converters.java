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

import java.util.Locale;

import com.google.common.base.Function;

/**
 * Converters provides expression converters for lower case, upper case and prefix/suffix conversions
 *
 * @author tiwe
 *
 */
@Deprecated
public final class Converters {

    private static final Constant<String> PERCENT = ConstantImpl.create("%");

    private final char escape;

    /**
     * Create a new Converters instance
     *
     * @param escape escape character to be used
     */
    public Converters(char escape) {
        this.escape = escape;
    }

    public final Function<Object,Object> toLowerCase =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                return OperationImpl.create(String.class, Ops.LOWER, (Expression)arg);
            } else {
                return arg.toString().toLowerCase(Locale.ENGLISH);
            }
        }
    };

    public final Function<Object,Object> toUpperCase =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                return OperationImpl.create(String.class, Ops.UPPER, (Expression)arg);
            } else {
                return arg.toString().toUpperCase(Locale.ENGLISH);
            }
        }
    };

    public final Function<Object,Object> toStartsWithViaLike =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                return OperationImpl.create(String.class, Ops.CONCAT, (Expression)arg, PERCENT);
            } else {
                return escapeForLike(arg.toString()) + "%";
            }
        }
    };

    public final Function<Object,Object> toStartsWithViaLikeLower =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, (Expression)arg, PERCENT);
                return OperationImpl.create(String.class, Ops.LOWER, concated);
            } else {
                return escapeForLike(arg.toString().toLowerCase(Locale.ENGLISH)) + "%";
            }
        }
    };

    public final Function<Object,Object> toEndsWithViaLike =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                return OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
            } else {
                return "%" + escapeForLike(arg.toString());
            }
        }
    };

    public final Function<Object,Object> toEndsWithViaLikeLower =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                return OperationImpl.create(String.class, Ops.LOWER, concated);
            } else {
                return "%" + escapeForLike(arg.toString().toLowerCase(Locale.ENGLISH));
            }
        }
    };

    public final Function<Object,Object> toContainsViaLike =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                return OperationImpl.create(String.class, Ops.CONCAT, concated, PERCENT);
            } else {
                return "%" + escapeForLike(arg.toString()) + "%";
            }
        }
    };

    public final Function<Object,Object> toContainsViaLikeLower =
        new Function<Object,Object>() {
        @Override
        public Object apply(Object arg) {
            if (arg instanceof Constant) {
                return ConstantImpl.create(apply(arg.toString()).toString());
            } else if (arg instanceof Expression) {
                Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                concated = OperationImpl.create(String.class, Ops.CONCAT, concated, PERCENT);
                return OperationImpl.create(String.class, Ops.LOWER, concated);
            } else {
                return "%" + escapeForLike(arg.toString().toLowerCase(Locale.ENGLISH)) + "%";
            }
        }
    };

    public String escapeForLike(String str) {
        final StringBuilder rv = new StringBuilder(str.length() + 3);
        for (int i = 0; i < str.length(); i++) {
            final char ch = str.charAt(i);
            if (ch == escape || ch == '%' || ch == '_') {
                rv.append(escape);
            }
            rv.append(ch);
        }
        return rv.toString();
    }

}
