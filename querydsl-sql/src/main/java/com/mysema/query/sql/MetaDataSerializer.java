/**
 * 
 */
package com.mysema.query.sql;

import static com.mysema.codegen.Symbols.NEW;

import java.io.IOException;

import com.mysema.codegen.CodeWriter;
import com.mysema.query.codegen.EntitySerializer;
import com.mysema.query.codegen.EntityType;
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
        super(new TypeMappings());
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
}