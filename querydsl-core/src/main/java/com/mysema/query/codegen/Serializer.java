/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.contracts.Contracts;
import com.mysema.query.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Serializer provides FreeMarker based serialization of querydsl types to Java sources
 * 
 * @author tiwe
 * @version $Id$
 */
@Contracts
public class Serializer {

    private final Configuration configuration;
    
    private final String templateLocation;

    Serializer(Configuration configuration, String template) {
        this.configuration = Assert.notNull(configuration,"configuration is null");
        this.templateLocation = Assert.notNull(template,"template is null");
    }

    public void serialize(String targetFolder, String namePrefix, Collection<ClassModel> entityTypes) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        for (ClassModel type : entityTypes) {
            String packageName = type.getPackageName();
            model.put("package", packageName);
            model.put("type", type);
            model.put("classSimpleName", type.getSimpleName());
            try {
                String path = packageName.replace('.', '/') + "/" + namePrefix + type.getSimpleName() + ".java";
                serialize(model, FileUtils.writerFor(new File(targetFolder, path)));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
    
    public void serialize(Map<String, Object> model, Writer writer) throws IOException, TemplateException {
        configuration.getTemplate(templateLocation).process(model, writer);
        writer.flush();
    }

}
