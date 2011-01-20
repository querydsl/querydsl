/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.inheritance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.AbstractTest;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;

public class Inheritance3Test extends AbstractTest{

    /*
     * TODO : map type variables to BeanModels
     */

    @QueryEntity
    public class GenericSupertype<A>{
        A field;
        Collection<A> fieldCol;
        Set<A> fieldSet;
        List<A> fieldList;
        Map<String,A> fieldMap1;
        Map<A,String> fieldMap2;

        String stringField;
    }

    @QueryEntity
    public class GenericSupertypeC<D extends Comparable<D>> extends GenericSupertype<D>{

    }

    @QueryEntity
    public class GenericSupertypeS extends GenericSupertypeC<String>{

    }
    
    @QueryEntity
    public class GenericSupertypeS2 extends GenericSupertype<String>{

    }

    @Test
    public void GenericSupertype() throws SecurityException, NoSuchFieldException{
        cl = QInheritance3Test_GenericSupertype.class;
        match(PSimple.class, "field");    
    }
    
    @Test
    public void GenericSupertypeC() throws SecurityException, NoSuchFieldException{
        cl = QInheritance3Test_GenericSupertypeC.class;
        match(PSimple.class, "field");    
    }
    
    @Test
    public void GenericSupertypeS() throws SecurityException, NoSuchFieldException{
        cl = QInheritance3Test_GenericSupertypeS.class;
        match(PString.class, "field");
    }
    
    @Test
    public void GenericSupertypeS2() throws SecurityException, NoSuchFieldException{
        cl = QInheritance3Test_GenericSupertypeS2.class;
        match(PString.class, "field");
    }

}
