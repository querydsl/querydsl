/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.io.Closeable;
import java.util.Iterator;

/**
 * CloseableIterator bundles Iterator and Closeable
 * 
 * @author tiwe
 * @version $Id$
 * @see java.util.Iterator
 * @see java.io.Closeable
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {

}
