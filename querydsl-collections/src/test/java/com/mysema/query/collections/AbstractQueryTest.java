/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;

/**
 * AbstractQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractQueryTest {
    protected Cat c1 = new Cat("Kitty");
    protected Cat c2 = new Cat("Bob");
    protected Cat c3 = new Cat("Alex");
    protected Cat c4 = new Cat("Francis");
    
    protected QCat cat = new QCat("cat");
    
    protected List<Cat> cats = Arrays.asList(c1, c2, c3, c4);
    
    protected List<Integer> ints = new ArrayList<Integer>();
    
    protected List<Integer> myInts = new ArrayList<Integer>();
    
    protected QCat mate = new QCat("mate");
    protected QCat offspr = new QCat("offspr");
    protected QCat otherCat = new QCat("otherCat");
}
