/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.codehaus.janino.ExpressionEvaluator;


/**
 * ProjectingIterator is a projecting iterator, which projects a 
 *
 * @author tiwe
 * @version $Id$
 */
public class ProjectingIterator<RT> extends WrappingIterator<RT>{
    private ExpressionEvaluator ev;
    public ProjectingIterator(Iterator<?> it,  ExpressionEvaluator ev) {
        super(it);
        try {
            this.ev = ev;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }
    public RT next() {
        try {
            return (RT) ev.evaluate((Object[]) nextFromOrig());
        } catch (InvocationTargetException e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }
    }        
}