/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.domain;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class GenericType<T extends ItemType> {
    T itemType;
}