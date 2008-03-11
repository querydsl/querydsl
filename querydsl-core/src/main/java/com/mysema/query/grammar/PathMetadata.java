/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Types.Path;

/**
 * 
 * PathMetadata provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public final class PathMetadata{
    private final String localName;
    private final Path<?> parent;
    private final String path;
    PathMetadata(Path<?> parent, String localName){
        this.parent = parent;
        this.path = parent.getMetadata().getPath() + "." + localName;
        this.localName = localName;
    }
    PathMetadata(String localName) {
        this.parent = null;
        this.path = localName;
        this.localName = localName;
    }
    public String getLocalName(){ return localName; }
    public Path<?> getParent(){ return parent; }
    public String getPath(){ return path;}
    public String toString(){ return path; }
}