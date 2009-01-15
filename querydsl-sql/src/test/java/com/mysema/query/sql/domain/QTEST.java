/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

public class QTEST extends Path.PEntity<java.lang.Object>{
	public final Path.PString name = _string("NAME");	
	
    public QTEST(java.lang.String path) {
      	super(java.lang.Object.class, "TEST", path);
    }
    public QTEST(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "TEST", metadata);
    }
}