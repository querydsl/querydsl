/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.mysema.commons.lang.Assert;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Serializer provides FreeMarker based serialization of querydsl types to Java sources
 * 
 * @author tiwe
 * @version $Id$
 */
public class Serializer {

    private final Configuration configuration;
    
    private final String templateLocation;

    Serializer(Configuration configuration, String template) {
        this.configuration = Assert.notNull(configuration);
        this.templateLocation = Assert.notNull(template);
    }

    public void serialize(Map<String, Object> model, Writer writer) throws IOException, TemplateException {
        configuration.getTemplate(templateLocation).process(model, writer);
        writer.flush();
    }

}
