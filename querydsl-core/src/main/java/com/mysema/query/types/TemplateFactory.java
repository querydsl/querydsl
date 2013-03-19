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

    private static final Pattern elementPattern = Pattern.compile("\\{%?%?\\d+[slu%]?%?\\}");

    /**
     * Default instance 
     */
    public static final TemplateFactory DEFAULT = new TemplateFactory('\\');

    private final Map<String,Template> cache = new ConcurrentHashMap<String,Template>();

    private final Converters converters;
    
    public TemplateFactory(char escape) {
        converters = new Converters(escape);
    }
    
    public Template create(String template) {
        if (cache.containsKey(template)) {
            return cache.get(template);
        }else{
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
                        transformer = converters.toEndsWithViaLikeLower;
                        str = str.substring(2);
                    } else {
                        transformer = converters.toEndsWithViaLike;
                        str = str.substring(1);
                    }

                }
                int strip = 0;
                switch (str.charAt(str.length()-1)) {
                case 'l' :
                    transformer = converters.toLowerCase;
                    strip = 1;
                    break;
                case 'u' :
                    transformer = converters.toUpperCase;
                    strip = 1;
                    break;
                case '%' :
                    if (transformer == null) {
                        if (str.charAt(str.length()-2) == '%') {
                            transformer = converters.toStartsWithViaLikeLower;
                            strip = 2;
                        }else{
                            transformer = converters.toStartsWithViaLike;
                            strip = 1;
                        }
                    }else{
                        if (str.charAt(str.length()-2) == '%') {
                            transformer = converters.toContainsViaLikeLower;
                            strip = 2;
                        }else{
                            transformer = converters.toContainsViaLike;
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

}
