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
import com.mysema.query.codegen.Serializer;
import com.mysema.query.sql.DefaultNamingStrategy;
import com.mysema.query.sql.MetaDataExporter;
import com.mysema.query.sql.MetaDataSerializer;
import com.mysema.query.sql.NamingStrategy;

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
     * @parameter required=true
     */
    private String jdbcDriver;

    /**
     * @parameter required=true
     */
    private String jdbcUrl;

    /**
     * @parameter
     */
    private String jdbcUser;

    /**
     * @parameter
     */
    private String jdbcPassword;

    /**
     * @parameter default-value="Q"
     */
    private String namePrefix;

    /**
     * @parameter required=true
     */
    private String packageName;

    /**
     * @parameter
     */
    private String schemaPattern;

    /**
     * @parameter
     */
    private String tableNamePattern;

    /**
     * @parameter required=true
     */
    private String targetFolder;

    /**
     * @parameter
     */
    private String namingStrategyClass;

    /**
     * @parameter default-value=false
     */
    private boolean exportBeans;

    /**
     * @parameter default-value=false
     */
    private boolean innerClassesForKeys;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()){
            project.addTestCompileSourceRoot(targetFolder);
        }else{
            project.addCompileSourceRoot(targetFolder);
        }
        NamingStrategy namingStrategy;
        if (namingStrategyClass != null){
            try {
                namingStrategy = (NamingStrategy) Class.forName(namingStrategyClass).newInstance();
            } catch (InstantiationException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (IllegalAccessException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            } catch (ClassNotFoundException e) {
                throw new MojoExecutionException(e.getMessage(),e);
            }
        }else{
            namingStrategy = new DefaultNamingStrategy();
        }
        Serializer serializer = new MetaDataSerializer(namePrefix, namingStrategy, innerClassesForKeys);

        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName(packageName);
        exporter.setTargetFolder(new File(targetFolder));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSerializer(serializer);
        if (exportBeans){
            exporter.setBeanSerializer(new BeanSerializer());
        }
        exporter.setSchemaPattern(schemaPattern);
        exporter.setTableNamePattern(tableNamePattern);

        try {
            Class.forName(jdbcDriver);
            Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
            try{
                exporter.export(conn.getMetaData());
            }finally{
                if (conn != null){
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

    protected boolean isForTest(){
        return false;
    }

}
