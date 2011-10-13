/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.maven;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.mysema.query.codegen.BeanSerializer;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.DefaultNamingStrategy;
import com.mysema.query.sql.MetaDataExporter;
import com.mysema.query.sql.NamingStrategy;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.types.Type;

/**
 * MetaDataExportMojo is a goal for MetaDataExporter usage
 *
 * @author tiwe
 */
public class AbstractMetaDataExportMojo extends AbstractMojo{

    /**
     * @parameter expression="${project}" readonly=true required=true
     */
    private MavenProject project;

    /**
     * JDBC driver class name
     * @parameter required=true
     */
    private String jdbcDriver;

    /**
     * JDBC connection url
     * @parameter required=true
     */
    private String jdbcUrl;

    /**
     * JDBC connection username
     * @parameter
     */
    private String jdbcUser;

    /**
     * JDBC connection password
     * @parameter
     */
    private String jdbcPassword;

    /**
     * name prefix for query-types (default: "Q")
     * @parameter default-value="Q"
     */
    private String namePrefix;

    /**
     * name prefix for query-types (default: "")
     * @parameter default-value=""
     */
    private String nameSuffix;

    /**
     * name prefix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanPrefix;

    /**
     * name prefix for bean types (default: "")
     * @parameter default-value=""
     */
    private String beanSuffix;

    /**
     * package name for sources
     * @parameter required=true
     */
    private String packageName;

    /**
     * package name for bean sources (default: packageName)
     * @parameter
     */
    private String beanPackageName;

    /**
     * schemaPattern a schema name pattern; must match the schema name
     *        as it is stored in the database; "" retrieves those without a schema;
     *        <code>null</code> means that the schema name should not be used to narrow
     *        the search (default: null)
     *
     * @parameter
     */
    private String schemaPattern;

    /**
     * tableNamePattern a table name pattern; must match the
    *        table name as it is stored in the database (default: null)
     *
     * @parameter
     */
    private String tableNamePattern;

    /**
     * target source folder to create the sources into (e.g. target/generated-sources/java)
     *
     * @parameter required=true
     */
    private String targetFolder;

    /**
     * namingstrategy class to override (default: DefaultNamingStrategy.class)
     *
     * @parameter
     */
    private String namingStrategyClass;

    /**
     * serialize beans as well
     *
     * @parameter default-value=false
     */
    private boolean exportBeans;

    /**
     * wrap key properties into inner classes (default: false)
     *
     * @parameter default-value=false
     */
    private boolean innerClassesForKeys;

    /**
     * export validation annotations (default: true)
     *
     * @parameter default-value=true
     */
    private boolean validationAnnotations;

    /**
     * @parameter
     */
    private String[] customTypes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()) {
            project.addTestCompileSourceRoot(targetFolder);
        } else {
            project.addCompileSourceRoot(targetFolder);
        }
        NamingStrategy namingStrategy;
        if (namingStrategyClass != null) {
            try {
                namingStrategy = (NamingStrategy) Class.forName(namingStrategyClass).newInstance();
            } catch (InstantiationException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (IllegalAccessException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            }
        } else {
            namingStrategy = new DefaultNamingStrategy();
        }
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
        exporter.setPackageName(packageName);
        exporter.setBeanPackageName(beanPackageName);
        exporter.setInnerClassesForKeys(innerClassesForKeys);
        exporter.setTargetFolder(new File(targetFolder));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSchemaPattern(schemaPattern);
        exporter.setTableNamePattern(tableNamePattern);
        exporter.setValidationAnnotations(validationAnnotations);
        if (exportBeans) {
            exporter.setBeanSerializer(new BeanSerializer());
        }
        String sourceEncoding = (String)project.getProperties().get("project.build.sourceEncoding");
        if (sourceEncoding != null) {
            exporter.setSourceEncoding(sourceEncoding);
        }
        
        try {
            if (customTypes != null) {
                Configuration configuration = new Configuration(SQLTemplates.DEFAULT);
                for (String cl : customTypes) {
                    configuration.register((Type<?>)Class.forName(cl).newInstance());
                }
                exporter.setConfiguration(configuration);
            }
        } catch (IllegalAccessException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }

        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            try{
                exporter.export(conn.getMetaData());
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    protected boolean isForTest() {
        return false;
    }

}
