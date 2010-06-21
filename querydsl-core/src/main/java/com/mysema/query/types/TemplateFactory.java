/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static com.mysema.query.types.Converter.toContainsViaLike;
import static com.mysema.query.types.Converter.toEndsWithViaLike;
import static com.mysema.query.types.Converter.toStartsWithViaLike;
import static com.mysema.query.types.Converter.toContainsViaLikeLower;
import static com.mysema.query.types.Converter.toEndsWithViaLikeLower;
import static com.mysema.query.types.Converter.toStartsWithViaLikeLower;
import static com.mysema.query.types.Converter.toLowerCase;
import static com.mysema.query.types.Converter.toUpperCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Template.Element;

/**
 * TemplateFactory is a factory for {@link Template} instances
 *
 * @author tiwe
 *
 */
@Immutable
public class TemplateFactory {

    private static final Pattern elementPattern = Pattern.compile("\\{%?%?\\d+[slu%]?%?\\}");

    public static final TemplateFactory DEFAULT = new TemplateFactory();

    private final Map<String,Template> cache = new HashMap<String,Template>();

    public Template create(String template){
        if (cache.containsKey(template)){
            return cache.get(template);
        }else{
            Matcher m = elementPattern.matcher(template);
            List<Element> elements = new ArrayList<Template.Element>();
            int end = 0;
            while (m.find()) {
                if (m.start() > end) {
                    elements.add(new Element(template.substring(end, m.start())));
                }
                String str = template.substring(m.start() + 1, m.end() - 1).toLowerCase(Locale.ENGLISH);
                boolean asString = false;
                Converter<?,?> converter = null;
                if (str.charAt(0) == '%'){
                    if (str.charAt(1) == '%'){
                    converter = toEndsWithViaLikeLower;
                    str = str.substring(2);
                    }else{
                    converter = toEndsWithViaLike;
                        str = str.substring(1);
                    }

                }
                int strip = 0;
                switch (str.charAt(str.length()-1)){
                  case 'l' :
                      converter = toLowerCase;
                      strip = 1;
                      break;
                  case 'u' :
                      converter = toUpperCase;
                      strip = 1;
                      break;
                  case '%' :
                      if (converter == null){
                      if (str.charAt(str.length()-2) == '%'){
                          converter = toStartsWithViaLikeLower;
                          strip = 2;
                      }else{
                          converter = toStartsWithViaLike;
                          strip = 1;
                      }
                      }else{
                      if (str.charAt(str.length()-2) == '%'){
                          converter = toContainsViaLikeLower;
                          strip = 2;
                      }else{
                          converter = toContainsViaLike;
                          strip = 1;
                      }
                      }
                      break;
                  case 's' :
                      asString = true;
                      strip = 1;
                      break;
                }
                if (strip > 0){
                    str = str.substring(0, str.length()-strip);
                }
                int index = Integer.parseInt(str);
                if (asString){
                    elements.add(new Element(index, true));
                }else if (converter != null){
                    elements.add(new Element(index, converter));
                }else{
                    elements.add(new Element(index, false));
                }
                end = m.end();
            }
            if (end < template.length()) {
                elements.add(new Element(template.substring(end)));
            }
            Template rv = new Template(template, elements);
            cache.put(template, rv);
            return rv;
        }
    }

}
