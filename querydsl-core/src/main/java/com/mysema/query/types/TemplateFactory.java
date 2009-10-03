/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jcip.annotations.Immutable;

import com.mysema.query.types.Template.Element;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.EString;

/**
 * @author tiwe
 *
 */
@Immutable
public class TemplateFactory {

    private static final Pattern elementPattern = Pattern.compile("\\{%?\\d+[slu%]?\\}");

    private final Converter<EString,EString> toLowerCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toLowerCase();
        } 
    };
    
    private final Converter<EString,EString> toUpperCase = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return arg.toUpperCase();
        } 
    };
    
    private final Converter<EString,EString> toStartsWithViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return escapeForLike(arg).append("%");
        } 
    };
    
    private final Converter<EString,EString> toEndsWithViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return escapeForLike(arg).prepend("%");
        } 
    };
    
    private final Converter<EString,EString> toContainsViaLike = new Converter<EString,EString>(){
        @Override
        public EString convert(EString arg) {
            return escapeForLike(arg).prepend("%").append("%");
        }
    };
    
    private final Map<String,Template> cache = new HashMap<String,Template>();
    
    @SuppressWarnings("unchecked")
    private EString escapeForLike(EString expr){
        if (expr instanceof Constant){
            String str = ((Constant<String>) expr).getConstant();
            if (str.contains("%") || str.contains("_")){
                str = str.replace("%", "\\%").replace("_", "\\_");
                return EString.create(str);
            }                
        }        
        return expr;
    }
    
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
                String str = template.substring(m.start() + 1, m.end() - 1).toLowerCase();
                boolean asString = false;
                Converter<?,?> converter = null;
                if (str.charAt(0) == '%'){
                    converter = toEndsWithViaLike;
                    str = str.substring(1);
                }                
                switch (str.charAt(str.length()-1)){
                  case 'l' : converter = toLowerCase; break;
                  case 'u' : converter = toUpperCase; break;
                  case '%' : converter = converter == null ? toStartsWithViaLike : toContainsViaLike; break;
                  case 's' : asString = true; break;
                }
                if (asString || (converter != null && converter != toEndsWithViaLike)){
                    str = str.substring(0, str.length()-1);
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
