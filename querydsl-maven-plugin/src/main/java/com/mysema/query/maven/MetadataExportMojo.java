/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.maven;

/**
 * @phase generate-sources
 * @goal export
 *
 */
public class MetadataExportMojo extends AbstractMetaDataExportMojo{

    protected boolean isForTest(){
        return false;
    }
}
