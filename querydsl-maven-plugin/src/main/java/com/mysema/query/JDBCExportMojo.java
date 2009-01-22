/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.mysema.query.sql.MetaDataExporter;

/**
 * Maven plugin for JDBC Metadata export
 *
 * @goal jdbc-export
 * @phase generate-sources
 */
public class JDBCExportMojo extends AbstractMojo{
    
    /** @parameter */
    private File hibernateProps;

    /** @parameter */
    protected String namePrefix;

    /** @parameter */
    protected String packageName;
    
    /** @parameter */
    protected boolean camelCase;
        
    /** @parameter */
    private String dsUrl;
    
    /** @parameter */
    private String dsUsername;
    
    /** @parameter */
    private String dsPassword;
    
    /** @parameter */
    private String dsDriverClassName;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {           
            if (hibernateProps != null){
                Properties p = new Properties();
                p.load(new FileInputStream(hibernateProps));
                dsUrl = p.getProperty("hibernate.connection.url");
                dsUsername = p.getProperty("hibernate.connection.username");
                dsPassword = p.getProperty("hibernate.connection.password");
                dsDriverClassName = p.getProperty("hibernate.connection.driver_class");
            }            
            executeInternal();
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            getLog().error(error, e);
            throw new MojoExecutionException(error, e);
        }
    }
    
    private void executeInternal() throws Exception{
        Class.forName(dsDriverClassName);
        Connection conn = DriverManager.getConnection(dsUrl, dsUsername, dsPassword);
        try{
            MetaDataExporter e = new MetaDataExporter();
            e.setTargetFolder("target/generated-sources/java");
            e.setNamePrefix(namePrefix);
            e.setPackageName(packageName);
            e.setCamelCase(camelCase);
            e.export(conn.getMetaData());    
        }finally{
            if (conn != null) conn.close();
        }
    }

}
