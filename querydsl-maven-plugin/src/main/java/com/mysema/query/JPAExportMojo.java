/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Maven plugin for JPA Metadata export
 *
 * @goal jpa-export
 * @phase generate-sources
 */
public class JPAExportMojo extends AbstractMojo{

    /** @parameter */
    private String destPackage;
    
    /** @parameter */
    private String dtoPackage;
    
    /** @parameter */
    private String namePrefix;
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        // TODO : execute APT
        
    }

}
