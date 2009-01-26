/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

public class QSURVEY extends Path.PEntity<java.lang.Object>{
	public final Path.PString name = _string("name");
	public final Path.PNumber<java.lang.Integer> id = _number("id",java.lang.Integer.class);
	
    public QSURVEY(java.lang.String path) {
      	super(java.lang.Object.class, "survey", path);
    }
    public QSURVEY(PathMetadata<?> metadata) {
     	super(java.lang.Object.class, "survey", metadata);
    }
}