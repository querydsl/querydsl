/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.domain;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;
import com.mysema.query.util.NotEmpty;

@SuppressWarnings("all")
public class QTEST extends PEntity<java.lang.Object> {
    
    public final PString name = createString("name");

    public QTEST(@NotEmpty java.lang.String path) {
        super(java.lang.Object.class, "test", PathMetadata.forVariable(path));
    }

    public QTEST(PathMetadata<?> metadata) {
        super(java.lang.Object.class, "test", metadata);
    }
}