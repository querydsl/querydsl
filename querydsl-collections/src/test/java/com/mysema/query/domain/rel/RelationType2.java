/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain.rel;

import java.util.List;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class RelationType2<D extends RelationType2<D>> {
    List<D> list;
}