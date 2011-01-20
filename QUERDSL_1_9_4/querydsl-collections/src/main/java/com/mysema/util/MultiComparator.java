/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

import java.io.Serializable;
import java.util.Comparator;

import com.mysema.codegen.Evaluator;

/**
 * MultiComparator compares 
 *
 * @author tiwe
 * @version $Id$
 */

public class MultiComparator<T> implements Comparator<T>, Serializable {

    @SuppressWarnings("unchecked")
    private static final Comparator<Object> naturalOrder = (Comparator)new NullSafeComparableComparator();

    private static final long serialVersionUID = 1121416260773566299L;

    private final boolean[] asc;

    private final Evaluator<Object[]> ev;

    public MultiComparator(Evaluator<Object[]> ev, boolean[] directions) {
        this.ev = ev;
        this.asc = directions.clone();
    }

    @Override
    public int compare(T o1, T o2) {
        if (o1.getClass().isArray()){
            return innerCompare(ev.evaluate((Object[])o1), ev.evaluate((Object[])o2));
        }else{
            return innerCompare(ev.evaluate(o1), ev.evaluate(o2));
        }
    }

    private int innerCompare(Object[] o1, Object[] o2) {
        for (int i = 0; i < o1.length; i++) {
            int res;
            if (o1[i] == null){
                res = o2[i] == null ? 0 : -1;
            }else if (o2[i] == null){
                res = 1;
            }else{
                res = naturalOrder.compare(o1[i], o2[i]);    
            }            
            if (res != 0) {
                return asc[i] ? res : -res;
            }
        }
        return 0;
    }

}
