/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.maven;

/**
 * @phase generate-sources
 * @goal test-export
 *
 */
public class TestMetadataExportMojo extends AbstractMetaDataExportMojo{
    
    protected boolean isForTest(){
        return true;
    }

}
