/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.TimePath;

public class QueryTypeTest extends AbstractTest{

    @QueryEntity
    public static class QueryTypeEntity{
        @QueryType(PropertyType.SIMPLE)
        public String stringAsSimple;

        @QueryType(PropertyType.COMPARABLE)
        public String stringAsComparable;

        @QueryType(PropertyType.DATE)
        public String stringAsDate;

        @QueryType(PropertyType.DATETIME)
        public String stringAsDateTime;

        @QueryType(PropertyType.TIME)
        public String stringAsTime;

        @QueryType(PropertyType.NONE)
        public String stringNotInQuerydsl;

    }

    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        cl = QQueryTypeTest_QueryTypeEntity.class;
        match(SimplePath.class, "stringAsSimple");
        match(ComparablePath.class, "stringAsComparable");
        match(DatePath.class, "stringAsDate");
        match(DateTimePath.class, "stringAsDateTime");
        match(TimePath.class, "stringAsTime");
    }
}
