package com.mysema.query.sql.ant;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.mysema.query.codegen.BeanSerializer;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.sql.DefaultNamingStrategy;
import com.mysema.query.sql.MetaDataExporter;
import com.mysema.query.sql.MetaDataSerializer;
import com.mysema.query.sql.NamingStrategy;

/**
 * AntMetaDataExporter exports JDBC metadata to Querydsl query types
 * 
 * @author tiwe
 *
 */
public class AntMetaDataExporter extends Task {

    private String jdbcDriverClass;
    
    private String dbUrl;
    
    private String dbUserName;
    
    private String dbPassword;

    private String namePrefix;
    
    private String targetPackage;
    
    private String targetSourceFolder;

    private String schemaPattern;
    
    private String tableNamePattern;
    
    @Override
    public void execute() throws BuildException {
        Connection dbConn = null;
        File targetPackagePath = new File(targetSourceFolder);

        try {
            Class.forName(jdbcDriverClass).newInstance();

            dbConn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

            NamingStrategy namingStrategy = new DefaultNamingStrategy();
            Serializer serializer = new MetaDataSerializer(namePrefix, namingStrategy);

            MetaDataExporter exporter = new MetaDataExporter(
                    namePrefix, targetPackage, targetPackagePath,
                    namingStrategy, serializer, new BeanSerializer());            
            exporter.setSchemaPattern(schemaPattern);
            exporter.setTableNamePattern(tableNamePattern);

            exporter.export(dbConn.getMetaData());

        } catch (RuntimeException e) {
            throw new BuildException(e);
        } catch (InstantiationException e) {
            throw new BuildException(e);
        } catch (IllegalAccessException e) {
            throw new BuildException(e);
        } catch (ClassNotFoundException e) {
            throw new BuildException(e);
        } catch (SQLException e) {
            throw new BuildException(e);
        } finally {
            if (dbConn != null) {
                try {
                    dbConn.close();
                } catch (SQLException e2) {
                    throw new BuildException(e2);
                }
            }
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getJdbcDriverClass() {
        return jdbcDriverClass;
    }

    public void setJdbcDriverClass(String jdbcDriverClass) {
        this.jdbcDriverClass = jdbcDriverClass;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public String getTargetSourceFolder() {
        return targetSourceFolder;
    }

    public void setTargetSourceFolder(String targetSourceFolder) {
        this.targetSourceFolder = targetSourceFolder;
    }
    
    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public String getSchemaPattern() {
        return schemaPattern;
    }

    public String getTableNamePattern() {
        return tableNamePattern;
    }
    
    
}