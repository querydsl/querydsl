/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.util.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Serializer provides FreeMarker based serialization of querydsl types to Java sources
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class Serializer {

    private final Configuration configuration;
    
    private final String templateLocation;

    Serializer(Configuration configuration, String template) {
        this.configuration = Assert.notNull(configuration,"configuration is null");
        this.templateLocation = Assert.notNull(template,"template is null");
    }
    
    /**
     * Serialize the given ClassModel 
     * 
     * @param type ClassModel to serialize
     * @param writer serialization target
     * @throws IOException
     * @throws TemplateException
     */
    public void serialize(ClassModel type, Writer writer) throws IOException, TemplateException {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("pre", type.getPrefix());
        model.put("package", type.getPackageName());
        model.put("type", type);
        model.put("classSimpleName",type.getSimpleName());
        configuration.getTemplate(templateLocation).process(model, writer);
        writer.flush();
    }
    
//    /**
//     * Serialize the given ClassModel instances
//     * 
//     * @param targetFolder
//     * @param prefix
//     * @param entityTypes
//     */
//    public void serialize(String targetFolder, String prefix, Class<?>... types) {
//        ClassModelFactory factory = new ClassModelFactory(new TypeModelFactory(Collections.singleton(o)));
//        for (Class<?> type : types) {            
//            try {
//                ClassModel model = factory.create(type, prefix);
//                String packageName = model.getPackageName();
//                String path = packageName.replace('.', File.separatorChar) 
//                    + File.separator 
//                    + model.getPrefix() + type.getSimpleName() + ".java";
//                serialize(model, FileUtils.writerFor(new File(targetFolder, path)));
//            } catch (Exception e) {
//                throw new RuntimeException(e.getMessage(), e);
//            }
//        }
//    }

}
