/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

public class QSURVEY extends Path.PEntity<java.lang.Object>{
	public final Path.PString name = _string("NAME");
	public final Path.PComparable<java.lang.Integer> id = _comparable("ID",java.lang.Integer.class);
	
    public QSURVEY(java.lang.String path) {
      	super(java.lang.Object.class, "SURVEY", path);
    }
    public QSURVEY(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "SURVEY", metadata);
    }
}