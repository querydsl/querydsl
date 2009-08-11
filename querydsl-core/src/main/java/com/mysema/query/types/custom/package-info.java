/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */

/**
 * Custom types are comparable to operations but contain the serialization 
 * templates instead of operator patterns. Custom types are always implementation specific.
 */
@DefaultAnnotationForMethods( { Nonnull.class,
        OverridingMethodsMustInvokeSuper.class })
package com.mysema.query.types.custom;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import edu.umd.cs.findbugs.annotations.DefaultAnnotationForMethods;

