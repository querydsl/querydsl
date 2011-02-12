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

    /**
     * JDBC driver class name
     */
    private String jdbcDriverClass;

    /**
     * JDBC connection url
     */
    private String dbUrl;

    /**
     * JDBC connection username
     */
    private String dbUserName;

    /**
     * JDBC connection password
     */
    private String dbPassword;

    /**
     * name prefix for generated query types (default: "Q")
     */
    private String namePrefix;
    
    /**
     * name suffix for generated query types (default: "")
     */
    private String nameSuffix;

    /**
     * target package to generate classes to
     */
    private String targetPackage;

    /**
     * target source folder
     */
    private String targetSourceFolder;

    /**
     * schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search (default: null)
     */
    private String schemaPattern;

    /**
     * tableNamePattern a table name pattern; must match the
     *        table name as it is stored in the database (default: null)
     */
    private String tableNamePattern;

    /**
     * wrap key properties into inner classes (default: false)
     */
    private boolean innerClassesForKeys;

    /**
     * serialize beans as well
     */
    private boolean exportBeans;

    @Override
    public void execute() throws BuildException {
        Connection dbConn = null;
        File targetPackagePath = new File(targetSourceFolder);

        try {
            Class.forName(jdbcDriverClass).newInstance();

            dbConn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

            NamingStrategy namingStrategy = new DefaultNamingStrategy();
            Serializer serializer = new MetaDataSerializer(namePrefix, namingStrategy, innerClassesForKeys);

            MetaDataExporter exporter = new MetaDataExporter();
            exporter.setNamePrefix(namePrefix);
            exporter.setNameSuffix(nameSuffix);
            exporter.setPackageName(targetPackage);
            exporter.setTargetFolder(targetPackagePath);
            exporter.setNamingStrategy(namingStrategy);
            exporter.setSerializer(serializer);
            if (exportBeans){
                exporter.setBeanSerializer(new BeanSerializer());
            }
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

    public boolean isInnerClassesForKeys() {
        return innerClassesForKeys;
    }

    public void setInnerClassesForKeys(boolean innerClassesForKeys) {
        this.innerClassesForKeys = innerClassesForKeys;
    }

    public boolean isExportBeans() {
        return exportBeans;
    }

    public void setExportBeans(boolean exportBeans) {
        this.exportBeans = exportBeans;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }
    
    

}