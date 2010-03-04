/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.io.Serializable;
import java.util.Comparator;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.comparators.ComparableComparator;



/**
 * MultiComparator compares
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class MultiComparator implements Comparator<Object[]>, Serializable {

    private static final long serialVersionUID = 1121416260773566299L;

    private static final Comparator<Object> naturalOrder = ComparableComparator.getInstance();

    private final Evaluator<Object[]> ev;

    private final boolean[] asc;

    public MultiComparator(Evaluator<Object[]> ev, boolean[] directions) {
        this.ev = ev;
        this.asc = directions.clone();
    }

    public int compare(Object[] o1, Object[] o2) {
        return innerCompare(ev.evaluate(o1), ev.evaluate(o2));
    }
    
    private int innerCompare(Object[] o1, Object[] o2) {
        for (int i = 0; i < o1.length; i++) {
            int res = naturalOrder.compare(o1[i], o2[i]);
            if (res != 0) {
                return asc[i] ? res : -res;
            }
        }
        return 0;
    }

}
