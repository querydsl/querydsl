/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.grammar.JavaOps;


/**
 * ColQuery is a Query implementation for querying on Java collections
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQuery extends AbstractColQuery<ColQuery>{

    public ColQuery(){
        super();
    }
    
    public ColQuery(JavaOps ops){
        super(ops);
    }
    
}
