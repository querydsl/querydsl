package com.mysema.query.codegen;

import java.io.IOException;
import java.io.Writer;

/**
 * @author tiwe
 *
 */
public class SupertypeSerializer extends EntitySerializer{

    public SupertypeSerializer(boolean embeddable) {
        super(embeddable);
    }
    
    @Override
    protected void defaultInstance(ClassModel model, StringBuilder builder) {
        // no default instance
    }
    
    @Override
    protected void constructors(ClassModel model, Writer writer) throws IOException {
        final String simpleName = model.getSimpleName();
        final String queryType = model.getPrefix() + simpleName;
        final String localName = model.getLocalName();
        
        StringBuilder builder = new StringBuilder();
        builder.append("    public " + queryType + "(PEntity<? extends "+localName+"> entity) {\n");
        builder.append("        super(entity.getType(), entity.getEntityName(), entity.getMetadata());\n");
        builder.append("    }\n\n");
        builder.append("    public " + queryType + "(PathMetadata<?> metadata) {\n");
        builder.append("        super("+ localName + ".class, \"" + simpleName + "\", metadata);\n");
        builder.append("    }\n\n");
        writer.append(builder.toString());
    }

}
