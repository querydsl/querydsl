/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.url;

import static com.mysema.query.types.path.PathMetadataFactory.forVariable;

import java.net.URL;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * @author tiwe
 *
 */
public class QURL extends PEntity<URL>{
    
    private static final long serialVersionUID = 9048088068716893900L;

    public final PString authority = createString("authority");

    public final PSimple<Object> content = createSimple("content",Object.class);
        
    public final PNumber<Integer> defaultPort = createNumber("defaultPort",Integer.class);
    
    public final PString file = createString("file");
    
    public final PString host = createString("host");
    
    public final PString path = createString("path");
    
    public final PNumber<Integer> port = createNumber("port",Integer.class);
    
    public final PString protocol = createString("protocol");
    
    public final PString query = createString("query");
    
    public final PString ref = createString("ref");
    
    public final PString userInfo = createString("userInfo");
    
    public QURL(PathMetadata<?> metadata) {
        super(URL.class, metadata);
    }
    
    public QURL(QURL parent, String property) {
        super(URL.class, PathMetadataFactory.forProperty(parent, property));
    }
    
    public QURL(String variable) {
        super(URL.class, forVariable(variable));
    }

}
