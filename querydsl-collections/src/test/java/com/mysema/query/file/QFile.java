/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.file;

import static com.mysema.query.types.path.PathMetadataFactory.forProperty;
import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.io.File;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PString;

/**
 * @author tiwe
 *
 */
public class QFile extends PComparable<File>{
    
    private static final long serialVersionUID = -7703329992523284173L;
    
    private static final String GET_CONTENT = "org.apache.commons.io.FileUtils.readFileToString({0}, {1})";

    public static final QFile any = new QFile("any");
    
    public static Iterable<File> walk(File dir){
        return new DirectoryWalk(dir);
    }
    
    public final PBoolean absolute = new PBoolean(this, "absolute");

    private volatile  QFile absoluteFile, canonicalFile, parentFile;
    
    public final PString absolutePath = new PString(this, "absolutePath");
    
    public final PString canonicalPath = new PString(this, "canonicalPath");
    
    public final PBoolean directory = new PBoolean(this, "directory");
    
    public final PBoolean file = new PBoolean(this, "file");
    
    public final PBoolean hidden = new PBoolean(this, "hidden");
    
    public final PString name = new PString(this, "name");

    public final PString parent = new PString(this, "parent");

    public final PString path = new PString(this, "path");

    public QFile(PathMetadata<?> metadata) {
        super(File.class, metadata);
    }

    public QFile(QFile parent, String property) {
        super(File.class, forProperty(parent,property));
    }
        
    public QFile(String variable) {
        super(File.class, forVariable(variable));
    }
    
    public QFile absoluteFile() {
        if (absoluteFile == null){
            absoluteFile = new QFile(this, "absoluteFile");
        }
        return absoluteFile;
    }
    
    public QFile canonicalFile() {
        if (canonicalFile == null){
            canonicalFile = new QFile(this, "canonicalFile");
        }
        return canonicalFile;
    }
    
    public EString getContent(String encoding){
        return CString.create(GET_CONTENT, this, EStringConst.create(encoding));
    }
    
    public QFile parentFile() {
        if (parentFile == null){
            parentFile = new QFile(this, "parentFile");
        }
        return parentFile;
    }
    
}
