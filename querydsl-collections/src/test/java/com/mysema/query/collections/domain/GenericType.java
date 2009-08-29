/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.domain;

import com.mysema.query.annotations.Entity;

@Entity
public class GenericType<T extends ItemType> {
    T itemType;
}