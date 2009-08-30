/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.List;

import com.mysema.query.util.FileUtils;

/**
 * @author tiwe
 *
 */
public abstract class AbstractSerializer implements Serializer{
    
    @Override
    public void serialize(String targetFolder, String prefix, List<Class<? extends Annotation>> entityAnnotations, Class<?>... types) {
        TypeModelFactory typeModelFactory = new TypeModelFactory(entityAnnotations);
        ClassModelFactory factory = new ClassModelFactory(typeModelFactory);
        for (Class<?> type : types) {            
            try {
                ClassModel model = factory.create(type, prefix);
                String packageName = model.getPackageName();
                String path = packageName.replace('.', File.separatorChar) 
                    + File.separator 
                    + model.getPrefix() + type.getSimpleName() + ".java";
                serialize(model, FileUtils.writerFor(new File(targetFolder, path)));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}
