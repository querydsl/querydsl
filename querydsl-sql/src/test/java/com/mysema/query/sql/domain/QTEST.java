/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

public class QTEST extends PEntity<java.lang.Object>{
	public final PString name = _string("name");	
	
    public QTEST(java.lang.String path) {
      	super(java.lang.Object.class, "test", path);
    }
    public QTEST(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "test", metadata);
    }
}