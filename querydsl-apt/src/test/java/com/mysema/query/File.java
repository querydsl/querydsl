/**
 * 
 */
package com.mysema.query;

import com.mysema.query.annotations.QueryExtensions;
import com.mysema.query.annotations.QueryMethod;

@QueryExtensions(java.io.File.class)
public interface File{
    
    @QueryMethod("{0}.getName()")
    String getName();
    
    @QueryMethod("{0}.getParent()")
    String getParent();
    
    @QueryMethod("{0}.getParentFile()")
    File getParentFile();
    
    @QueryMethod("{0}.getPath()")
    String getPath();
    
    @QueryMethod("{0}.isDirectory()")
    boolean isDirectory();
    
    @QueryMethod("{0}.isFile()")
    boolean isFile();
    
    @QueryMethod("{0}.lastModified()")
    long lastModified();
    
    @QueryMethod("{0}.length()")
    long length();
    
    @QueryMethod("{0}.getAbsolutePath()")
    String getAbsolutePath();
    
    @QueryMethod("{0}.getAbsolutFile()")
    File getAbsoluteFile();
    
    @QueryMethod("{0}.getCanonicalPath()")
    String getCanonicalPath();
    
    @QueryMethod("{0}.getCanonicalFile()")
    File getCanonicalFile();
    
    
}