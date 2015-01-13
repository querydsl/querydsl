/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.codegen.ant;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Comparator;

import com.mysema.codegen.model.SimpleType;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.MetaDataExporter;
import com.querydsl.sql.codegen.NamingStrategy;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * AntMetaDataExporter exports JDBC metadata to Querydsl querydsl types
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
     * name prefix for generated querydsl types (default: "Q")
     */
    private String namePrefix;

    /**
     * name suffix for generated querydsl types (default: "")
     */
    private String nameSuffix;

    /**
     * name prefix for generated bean types (default: "Q")
     */
    private String beanPrefix;

    /**
     * name suffix for generated bean types (default: "")
     */
    private String beanSuffix;

    /**
     * target package to generate classes to
     */
    private String targetPackage;

    /**
     * target package to generated bean classes to (default: targetPackage)
     */
    private String beanTargetPackage;

    /**
     * target source folder
     */
    private String targetSourceFolder;

    /**
     * schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        {@code null} means that the schema name should not be used to narrow
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

    /**
     * export validation annotations (@NotNull, @Size etc)
     */
    private boolean validationAnnotations = false;

    /**
     * charset encoding of the sources to be generated
     */
    private String sourceEncoding;

    /**
     *
     */
    private boolean columnAnnotations = false;

    /**
     *
     */
    private boolean schemaToPackage = false;

    /**
     *
     */
    private boolean lowerCase = false;

    /**
     *
     */
    private boolean exportTables = true;

    /**
     *
     */
    private boolean exportViews = true;

    /**
     *
     */
    private boolean exportAll = false;

    /**
     *
     */
    private boolean exportPrimaryKeys = true;

    /**
     *
     */
    private boolean exportForeignKeys = true;

    /**
     *
     */
    private String[] beanInterfaces;

    /**
     *
     */
    private boolean beanAddToString;

    /**
     *
     */
    private boolean beanAddFullConstructor;

    /**
     *
     */
    private boolean beanPrintSupertype;
    
    /**
     * override default column order (default: alphabetical)
     */
    private String columnComparatorClass;

    /**
     *
     */
    private boolean spatial;
    
    /**
     * comma separated list of "table types" to export.
     * ex: "TABLE, VIEW, MATERIALIZED VIEW"
     * 
     * @parameter
     */
    private String tableTypesToExport;
    
    /**
     * java import added to generated querydsl classes:
     * com.bar for package (without .* notation)
     * com.bar.Foo for class
     *
     */
    private String[] imports;


    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void execute() {
        Connection dbConn = null;
        File targetPackagePath = new File(targetSourceFolder);

        try {
            Class.forName(jdbcDriverClass).newInstance();

            dbConn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

            NamingStrategy namingStrategy = new DefaultNamingStrategy();
            MetaDataExporter exporter = new MetaDataExporter();
            if (namePrefix != null) {
                exporter.setNamePrefix(namePrefix);
            }
            if (nameSuffix != null) {
                exporter.setNameSuffix(nameSuffix);
            }
            if (beanPrefix != null) {
                exporter.setBeanPrefix(beanPrefix);
            }
            if (beanSuffix != null) {
                exporter.setBeanSuffix(beanSuffix);
            }
            exporter.setPackageName(targetPackage);
            exporter.setBeanPackageName(beanTargetPackage);
            exporter.setTargetFolder(targetPackagePath);
            exporter.setNamingStrategy(namingStrategy);
            exporter.setInnerClassesForKeys(innerClassesForKeys);
            exporter.setSchemaPattern(schemaPattern);
            exporter.setTableNamePattern(tableNamePattern);
            exporter.setColumnAnnotations(columnAnnotations);
            exporter.setValidationAnnotations(validationAnnotations);
            exporter.setSchemaToPackage(schemaToPackage);
            exporter.setLowerCase(lowerCase);
            exporter.setExportTables(exportTables);
            exporter.setExportViews(exportViews);
            exporter.setExportAll(exportAll);
            exporter.setTableTypesToExport(tableTypesToExport);
            exporter.setExportPrimaryKeys(exportPrimaryKeys);
            exporter.setExportForeignKeys(exportForeignKeys);
            exporter.setSpatial(spatial);

            if (imports != null && imports.length > 0) {
                exporter.setImports(imports);
            }

            if (exportBeans) {
                BeanSerializer serializer = new BeanSerializer();
                if (beanInterfaces != null) {
                    for (String iface : beanInterfaces) {
                        int sepIndex = iface.lastIndexOf('.');
                        if (sepIndex < 0) {
                            serializer.addInterface(new SimpleType(iface));
                        } else {
                            String packageName = iface.substring(0, sepIndex);
                            String simpleName = iface.substring(sepIndex + 1);
                            serializer.addInterface(new SimpleType(iface, packageName, simpleName));
                        }
                    }
                }
                serializer.setAddFullConstructor(beanAddFullConstructor);
                serializer.setAddToString(beanAddToString);
                serializer.setPrintSupertype(beanPrintSupertype);
                exporter.setBeanSerializer(serializer);
            }
            if (sourceEncoding != null) {
                exporter.setSourceEncoding(sourceEncoding);
            }
            if (columnComparatorClass != null) {
                exporter.setColumnComparatorClass((Class) Class.forName(this.columnComparatorClass).asSubclass(Comparator.class));
            }

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

    public String getBeanPrefix() {
        return beanPrefix;
    }

    public void setBeanPrefix(String beanPrefix) {
        this.beanPrefix = beanPrefix;
    }

    public String getBeanSuffix() {
        return beanSuffix;
    }

    public void setBeanSuffix(String beanSuffix) {
        this.beanSuffix = beanSuffix;
    }

    public String getBeanTargetPackage() {
        return beanTargetPackage;
    }

    public void setBeanTargetPackage(String beanTargetPackage) {
        this.beanTargetPackage = beanTargetPackage;
    }

    public boolean isValidationAnnotations() {
        return validationAnnotations;
    }

    public void setValidationAnnotations(boolean validationAnnotations) {
        this.validationAnnotations = validationAnnotations;
    }

    public String getSourceEncoding() {
        return sourceEncoding;
    }

    public void setSourceEncoding(String sourceEncoding) {
        this.sourceEncoding = sourceEncoding;
    }

    public boolean isColumnAnnotations() {
        return columnAnnotations;
    }

    public void setColumnAnnotations(boolean columnAnnotations) {
        this.columnAnnotations = columnAnnotations;
    }

    public boolean isSchemaToPackage() {
        return schemaToPackage;
    }

    public void setSchemaToPackage(boolean schemaToPackage) {
        this.schemaToPackage = schemaToPackage;
    }

    public boolean isLowerCase() {
        return lowerCase;
    }

    public void setLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    public boolean isExportTables() {
        return exportTables;
    }

    public void setExportTables(boolean exportTables) {
        this.exportTables = exportTables;
    }

    public boolean isExportViews() {
        return exportViews;
    }

    public void setExportViews(boolean exportViews) {
        this.exportViews = exportViews;
    }

    public boolean isExportAll() {
        return exportAll;
    }

    public void setExportAll(boolean exportAll) {
        this.exportAll = exportAll;
    }

    public boolean isExportPrimaryKeys() {
        return exportPrimaryKeys;
    }

    public void setExportPrimaryKeys(boolean exportPrimaryKeys) {
        this.exportPrimaryKeys = exportPrimaryKeys;
    }

    public boolean isExportForeignKeys() {
        return exportForeignKeys;
    }

    public void setExportForeignKeys(boolean exportForeignKeys) {
        this.exportForeignKeys = exportForeignKeys;
    }

    public String[] getImports() {
        return imports;
    }

    public void setImports(String[] imports) {
        this.imports = imports;
    }

    public boolean isSpatial() {
        return spatial;
    }

    public void setSpatial(boolean spatial) {
        this.spatial = spatial;
    }

}