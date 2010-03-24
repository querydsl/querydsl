package com.mysema.query.file;

import static com.mysema.query.types.path.PathMetadataFactory.forProperty;
import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.io.File;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PString;

/**
 * @author tiwe
 *
 */
public class QFile extends PComparable<File>{
    
    public static final QFile any = new QFile("any");
    
    private static final long serialVersionUID = -7703329992523284173L;

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
    
    public QFile parentFile() {
        if (parentFile == null){
            parentFile = new QFile(this, "parentFile");
        }
        return parentFile;
    }
    
}
