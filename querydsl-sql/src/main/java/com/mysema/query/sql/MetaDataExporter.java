package com.mysema.query.sql;

import static com.mysema.query.apt.APTUtils.writerFor;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.apt.Field;
import com.mysema.query.apt.Serializer;
import com.mysema.query.apt.Type;
import com.mysema.query.apt.general.GeneralProcessor;

/**
 * MetadataExporter provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporter {
    
    private Map<String,Class<?>> sqlToJavaType = new HashMap<String,Class<?>>();
   
    {
        sqlToJavaType.put("BIGINT", Long.class);
        sqlToJavaType.put("BOOLEAN", Boolean.class);        
        sqlToJavaType.put("INTEGER", Integer.class);
        sqlToJavaType.put("SMALLINT", Byte.class);
        sqlToJavaType.put("TIMESTAMP", java.util.Date.class);
        sqlToJavaType.put("VARCHAR", String.class);
    }
    
    private boolean camelCase;
    
    private String namePrefix = "", targetFolder, packageName;
    
    private String schemaPattern, tableNamePattern;
    
    private Serializer serializer = GeneralProcessor.DOMAIN_OUTER_TMPL;
    
    public void export(DatabaseMetaData md) throws SQLException{
        if (targetFolder == null) throw new IllegalArgumentException("targetFolder needs to be set");
        if (packageName == null) throw new IllegalArgumentException("packageName needs to be set");
        
        ResultSet tables = md.getTables(null, schemaPattern, tableNamePattern, null);
        while (tables.next()){
            String tableName = tables.getString(3);
            Type type = new Type(null, "java.lang.Object",tableName);
            ResultSet columns = md.getColumns(null, schemaPattern, tables.getString(3), null);
            while (columns.next()){
                String _name = columns.getString(4);
                if (camelCase){
                    _name = toCamelCase(_name,false);
                }
                Class<?> _class = sqlToJavaType.get(columns.getString(6));
                if (_class == null) throw new RuntimeException("No java type for " + columns.getString(6));
                Field.Type _type;
                if (_class.equals(Boolean.class) || _class.equals(boolean.class)){
                    _type = Field.Type.BOOLEAN;
                }else if (_class.equals(String.class)){
                    _type = Field.Type.STRING;
                }else{
                    _type = Field.Type.COMPARABLE;
                }
                type.addField(new Field(
                        _name, columns.getString(4), null, 
                        _class.getName(), _class.getSimpleName(), _type));
            }
            columns.close();
            serialize(type);
        }
        tables.close();
    }
    
    private void serialize(Type type) {
        // populate model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pre", namePrefix);
        model.put("package", packageName);
        model.put("type", type);
        model.put("classSimpleName", type.getSimpleName());

        // serialize it
        try {
            String path = packageName.replace('.', '/') + "/" + namePrefix
                    + type.getSimpleName() + ".java";
            serializer.serialize(model, writerFor(new File(targetFolder, path)));
        } catch (Exception e) {
            throw new RuntimeException("Caught exception", e);
        }
    }

    public void setCamelCase(boolean b) {
        this.camelCase = b;        
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    private String toCamelCase(String name, boolean firstCapitalized) {
        StringBuilder builder = new StringBuilder(name.length());
        boolean caps = firstCapitalized;
        for (char c : name.toLowerCase().toCharArray()){
            if (c == '_'){
                caps = true; continue;
            }else if (caps){
                caps = false; builder.append(Character.toUpperCase(c));
            }else{
                builder.append(c);
            }
        }
        return builder.toString();
    }
    
}
