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
import freemarker.template.TemplateException;

/**
 * Serializer is the main interface to be implemented for code generating serializers.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Serializer {

    /**
     * Serialize the given context to the given Writer instance
     * 
     * @param model
     * @param writer
     * @throws Exception
     */
    public void serialize(Map<String, Object> model, Writer writer)
            throws Exception;

    public final class FreeMarker implements Serializer {

        private static final Configuration cfg;

        static {
            cfg = new Configuration();
            cfg.setClassForTemplateLoading(Serializer.class, "/");
            cfg.setObjectWrapper(new DefaultObjectWrapper());
        }

        private final String templateLocation;

        public FreeMarker(String template) {
            if (template == null)
                throw new IllegalArgumentException("template was null");
            templateLocation = template;
        }

        public void serialize(Map<String, Object> model, Writer writer)
                throws IOException, TemplateException {
            cfg.getTemplate(templateLocation).process(model, writer);
            writer.flush();
        }

    }

}
