/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static com.mysema.codegen.Symbols.NEW;

import java.io.IOException;
import java.util.Collections;

import com.mysema.codegen.CodeWriter;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.SerializerConfig;
import com.mysema.query.codegen.TypeMappings;

/**
 * MetaDataSerializer defines the Query type serialization logic for MetaDataExporter.
 * Subclass this class for customization.
 * 
 * @author tiwe
 *
 */
public class MetaDataSerializer extends EntitySerializer {
    
    private final String namePrefix;
    
    private final NamingStrategy namingStrategy;

    public MetaDataSerializer(String namePrefix, NamingStrategy namingStrategy) {
	// TODO : supply reserved SQL keywords
        super(new TypeMappings(),Collections.<String>emptyList());
        this.namePrefix = namePrefix;
        this.namingStrategy = namingStrategy;
    }

    @Override 
    protected void introDefaultInstance(CodeWriter writer, EntityType entityType) throws IOException {
        String variableName = namingStrategy.getDefaultVariableName(namePrefix, entityType);
        String alias = namingStrategy.getDefaultAlias(namePrefix, entityType);
        String queryType = typeMappings.getPathType(entityType, entityType, true);            
        writer.publicStaticFinal(queryType, variableName, NEW + queryType + "(\"" + alias + "\")");
    }
    
    @Override
    protected void introImports(CodeWriter writer, SerializerConfig config, EntityType model) throws IOException {
        super.introImports(writer, config, model);
        writer.imports(Table.class);
    }
}