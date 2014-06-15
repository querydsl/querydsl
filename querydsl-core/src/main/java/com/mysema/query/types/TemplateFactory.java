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

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.mysema.query.types.Template.Element;

/**
 * TemplateFactory is a factory for {@link Template} instances
 *
 * @author tiwe
 *
 */
public class TemplateFactory {

    public static final TemplateFactory DEFAULT = new TemplateFactory('\\');

    private static final Constant<String> PERCENT = ConstantImpl.create("%");

    private static final Pattern elementPattern = Pattern.compile("\\{%?%?\\d+[slu%]?%?\\}");

    private final Map<String,Template> cache = new ConcurrentHashMap<String,Template>();

    private final char escape;

    private final Function<Object,Object> toLowerCase =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        return OperationImpl.create(String.class, Ops.LOWER, (Expression)arg);
                    } else {
                        return String.valueOf(arg).toLowerCase(Locale.ENGLISH);
                    }
                }
            };

    private final Function<Object,Object> toUpperCase =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        return OperationImpl.create(String.class, Ops.UPPER, (Expression)arg);
                    } else {
                        return String.valueOf(arg).toUpperCase(Locale.ENGLISH);
                    }
                }
            };

    private final Function<Object,Object> toStartsWithViaLike =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        return OperationImpl.create(String.class, Ops.CONCAT, (Expression)arg, PERCENT);
                    } else {
                        return escapeForLike(String.valueOf(arg)) + "%";
                    }
                }
            };

    private final Function<Object,Object> toStartsWithViaLikeLower =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, (Expression)arg, PERCENT);
                        return OperationImpl.create(String.class, Ops.LOWER, concated);
                    } else {
                        return escapeForLike(String.valueOf(arg).toLowerCase(Locale.ENGLISH)) + "%";
                    }
                }
            };

    private final Function<Object,Object> toEndsWithViaLike =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        return OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                    } else {
                        return "%" + escapeForLike(String.valueOf(arg));
                    }
                }
            };

    private final Function<Object,Object> toEndsWithViaLikeLower =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                        return OperationImpl.create(String.class, Ops.LOWER, concated);
                    } else {
                        return "%" + escapeForLike(String.valueOf(arg).toLowerCase(Locale.ENGLISH));
                    }
                }
            };

    private final Function<Object,Object> toContainsViaLike =
            new Function<Object,Object>() {
                @Override
                public Object apply(Object arg) {
                    if (arg instanceof Constant) {
                        return ConstantImpl.create(apply(arg.toString()).toString());
                    } else if (arg instanceof Expression) {
                        Expression<String> concated = OperationImpl.create(String.class, Ops.CONCAT, PERCENT, (Expression)arg);
                        return OperationImpl.create(String.class, Ops.CONCAT, concated, PERCENT);
                    } else {
                        return "%" + escapeForLike(String.valueOf(arg)) + "%";
                    }
                }
            };

    private final Function<Object,Object> toContainsViaLikeLower =
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
                        return "%" + escapeForLike(String.valueOf(arg).toLowerCase(Locale.ENGLISH)) + "%";
                    }
                }
            };

    public TemplateFactory(char escape) {
        this.escape = escape;
    }
    
    public Template create(String template) {
        if (cache.containsKey(template)) {
            return cache.get(template);
        } else {
            Matcher m = elementPattern.matcher(template);
            final ImmutableList.Builder<Element> elements = ImmutableList.builder();
            int end = 0;
            while (m.find()) {
                if (m.start() > end) {
                    elements.add(new Template.StaticText(template.substring(end, m.start())));
                }
                String str = template.substring(m.start() + 1, m.end() - 1).toLowerCase(Locale.ENGLISH);
                boolean asString = false;
                Function<Object, Object> transformer = null;
                if (str.charAt(0) == '%') {
                    if (str.charAt(1) == '%') {
                        transformer = toEndsWithViaLikeLower;
                        str = str.substring(2);
                    } else {
                        transformer = toEndsWithViaLike;
                        str = str.substring(1);
                    }

                }
                int strip = 0;
                switch (str.charAt(str.length()-1)) {
                case 'l' :
                    transformer = toLowerCase;
                    strip = 1;
                    break;
                case 'u' :
                    transformer = toUpperCase;
                    strip = 1;
                    break;
                case '%' :
                    if (transformer == null) {
                        if (str.charAt(str.length()-2) == '%') {
                            transformer = toStartsWithViaLikeLower;
                            strip = 2;
                        } else {
                            transformer = toStartsWithViaLike;
                            strip = 1;
                        }
                    } else {
                        if (str.charAt(str.length()-2) == '%') {
                            transformer = toContainsViaLikeLower;
                            strip = 2;
                        } else {
                            transformer = toContainsViaLike;
                            strip = 1;
                        }
                    }
                    break;
                case 's' :
                    asString = true;
                    strip = 1;
                    break;
                }
                if (strip > 0) {
                    str = str.substring(0, str.length()-strip);
                }
                int index = Integer.parseInt(str);
                if (asString) {
                    elements.add(new Template.AsString(index));
                } else if (transformer != null) {
                    elements.add(new Template.Transformed(index, transformer));
                } else {
                    elements.add(new Template.ByIndex(index));
                }
                end = m.end();
            }
            if (end < template.length()) {
                elements.add(new Template.StaticText(template.substring(end)));
            }
            Template rv = new Template(template, elements.build());
            cache.put(template, rv);
            return rv;
        }
    }

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
