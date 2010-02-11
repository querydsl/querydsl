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
