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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.mysema.query.sql.MetaDataExporter;

/**
 * Maven plugin for Jdbc Metadata export
 *
 * @goal jdbc-export
 * @phase generate-sources
 */
public class JdbcMdExportMojo extends AbstractExportMojo{
    
    /** @parameter */
    private File props;
    
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
            expandProperties(new FileInputStream(props));
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
