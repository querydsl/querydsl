package com.mysema.query.sql;

import java.io.IOException;
import java.util.Collection;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Types;
import com.mysema.query.codegen.BeanSerializer;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * ExtendedBeanSerialzier which outputs primary key based equals, hashCode and toString implementations
 * 
 * @author tiwe
 *
 */
public class ExtendedBeanSerializer extends BeanSerializer {

    private static final Parameter o = new Parameter("o", Types.OBJECT);
    
    @Override
    protected void bodyEnd(EntityType model, CodeWriter writer) throws IOException {
        Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) model.getData().get(PrimaryKeyData.class);
        
        // equals
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.BOOLEAN_P, "equals", o);
        writer.line("return super.equals(o);");
        writer.end();
        
        // hashCode
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.INT, "hashCode");
        writer.line("return super.hashCode();");
        writer.end();
        
        // toString
        writer.annotation(Override.class);
        writer.beginPublicMethod(Types.STRING, "toString");
        writer.line("return super.toString();");
        writer.end();
        
    }
    
}
