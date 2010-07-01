package com.mysema.query.sql.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tiwe
 *
 */
public class KeyDataFactory {
    
    private static final int FK_FOREIGN_COLUMN_NAME = 8;

    private static final int FK_FOREIGN_TABLE_NAME = 7;

    private static final int FK_NAME = 12;

    private static final int FK_PARENT_COLUMN_NAME = 4;

    private static final int FK_PARENT_TABLE_NAME = 3;
    
    private static final int PK_COLUMN_NAME = 4;

    private static final int PK_NAME = 6;
    
    public Map<String, InverseForeignKeyData> getExportedKeys(DatabaseMetaData md, 
            String schemaPattern, String tableName) throws SQLException{
        ResultSet foreignKeys = md.getExportedKeys(null, schemaPattern, tableName);
        Map<String,InverseForeignKeyData> inverseForeignKeyData = new HashMap<String,InverseForeignKeyData>();
        try{
            while (foreignKeys.next()){
                String name = foreignKeys.getString(FK_NAME);
                String parentColumnName = foreignKeys.getString(FK_PARENT_COLUMN_NAME);
                String foreignTableName = foreignKeys.getString(FK_FOREIGN_TABLE_NAME);
                String foreignColumn = foreignKeys.getString(FK_FOREIGN_COLUMN_NAME);
                InverseForeignKeyData data = inverseForeignKeyData.get(name);
                if (data == null){
                    data = new InverseForeignKeyData(name, foreignTableName);
                    inverseForeignKeyData.put(name, data);
                }
                data.add(parentColumnName, foreignColumn);
            }
            return inverseForeignKeyData;
        }finally{
            foreignKeys.close();
        }    
    }

    public Map<String, ForeignKeyData> getImportedKeys(DatabaseMetaData md,
            String schemaPattern, String tableName) throws SQLException {
        ResultSet foreignKeys = md.getImportedKeys(null, schemaPattern, tableName);
        Map<String,ForeignKeyData> foreignKeyData = new HashMap<String,ForeignKeyData>();
        try{
            while (foreignKeys.next()){
                String name = foreignKeys.getString(FK_NAME);
                String parentTableName = foreignKeys.getString(FK_PARENT_TABLE_NAME);
                String parentColumnName = foreignKeys.getString(FK_PARENT_COLUMN_NAME);
                String foreignColumn = foreignKeys.getString(FK_FOREIGN_COLUMN_NAME);
                ForeignKeyData data = foreignKeyData.get(name);
                if (data == null){
                    data = new ForeignKeyData(name, parentTableName);
                    foreignKeyData.put(name, data);
                }
                data.add(foreignColumn, parentColumnName);
            }
            return foreignKeyData;
        }finally{
            foreignKeys.close();
        }
    }

    public Map<String, PrimaryKeyData> getPrimaryKeys(DatabaseMetaData md,
            String schemaPattern, String tableName) throws SQLException {
        ResultSet primaryKeys = md.getPrimaryKeys(null, schemaPattern, tableName);
        Map<String,PrimaryKeyData> primaryKeyData = new HashMap<String,PrimaryKeyData>();
        try{
            while (primaryKeys.next()){
                String name = primaryKeys.getString(PK_NAME);
                String columnName = primaryKeys.getString(PK_COLUMN_NAME);

                PrimaryKeyData data = primaryKeyData.get(name);
                if (data == null){
                    data = new PrimaryKeyData(name);
                    primaryKeyData.put(name, data);
                }
                data.add(columnName);
            }
            return primaryKeyData;
        }finally{
            primaryKeys.close();
        }
    }

}
