package com.mysema.query.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.BeanSerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.Property;
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * ExtendedBeanSerialzier which outputs primary key based equals, hashCode and toString implementations
 * 
 * @author tiwe
 *
 */
public class ExtendedBeanSerializer extends BeanSerializer {

    private static final Parameter o = new Parameter("o", Types.OBJECT);
    
    @SuppressWarnings("unchecked")
    @Override
    protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        
        if (primaryKeys == null || primaryKeys.isEmpty()) {
            return;
        }
        
        Map<String, Property> columnToProperty = new HashMap<String, Property>();
        for (Property property : model.getProperties()) {
            columnToProperty.put(property.getAnnotation(Column.class).value(), property);
        }
        
        StringBuilder anyColumnIsNull = new StringBuilder();
        StringBuilder columnEquals = new StringBuilder();
        StringBuilder toString = new StringBuilder();
        List<String> properties = new ArrayList<String>();
        for (PrimaryKeyData pk : primaryKeys) {
            for (String column : pk.getColumns()) {
                Property property = columnToProperty.get(column);
                String propName = property.getEscapedName();
                if (anyColumnIsNull.length() > 0) {
                    anyColumnIsNull.append(" || ");
                    columnEquals.append(" && ");
                    toString.append("+ \";\" + ");
                } else {
                    toString.append("\"" + model.getSimpleName() + "#\" + ");
                }
                anyColumnIsNull.append(propName + " == null");
                columnEquals.append(propName + ".equals(obj." + propName + ")");
                toString.append(propName);
                properties.add(propName);
            }
        }
        
        // equals
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.BOOLEAN_P, "equals", o);
        writer.line("if (", anyColumnIsNull + ") {");        
        writer.line("    return super.equals(o);");
        writer.line("}");
        writer.line("if (!(o instanceof ", model.getSimpleName(), ")) {");
        writer.line("    return false;");
        writer.line("}");
        writer.line(model.getSimpleName(), " obj = (", model.getSimpleName(), ")o;");
        writer.line("return ", columnEquals + ";");
        writer.end();
        
        // hashCode
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.INT, "hashCode");
        writer.line("if (", anyColumnIsNull + ") {");        
        writer.line("    return super.hashCode();");
        writer.line("}");
        writer.line("final int prime = 31;");
        writer.line("int result = 1;");
        for (String property : properties) {
            writer.line("result = prime * result + ", property, ".hashCode();");
        }
        writer.line("return result;");
        writer.end();
        
        // toString
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.STRING, "toString");
//        writer.line("if (", anyColumnIsNull + ") {");      
//        writer.line("    return super.toString();");
//        writer.line("}");
        writer.line("return ", toString + ";");
        writer.end();
        
    }
    
}
