/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.maven;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.mysema.query.sql.MetaDataExporter;

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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isForTest()){
            project.addTestCompileSourceRoot(targetFolder);
        }else{
            project.addCompileSourceRoot(targetFolder);    
        }
        
        MetaDataExporter exporter = new MetaDataExporter(namePrefix, packageName, schemaPattern, tableNamePattern, targetFolder);
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
        } catch (Exception e) {
            getLog().error(e);
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    

    protected boolean isForTest(){
        return false;
    }

}
