/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarkerSerializer provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class FreeMarkerSerializer {
    
    private final Configuration cfg;
    
    private final String templateLocation;
    
    public FreeMarkerSerializer(String template){
        if (template == null) throw new IllegalArgumentException("template was null");
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        templateLocation = template;
    }
    
    public void serialize(Map<String,Object> model, Writer writer) throws IOException, TemplateException{
        Template template = cfg.getTemplate(templateLocation);         
        template.process(model, writer);
        writer.flush();  
    }

}
