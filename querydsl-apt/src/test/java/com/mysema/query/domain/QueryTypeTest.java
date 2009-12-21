package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryType;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PTime;

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
        match(PSimple.class, "stringAsSimple");
        match(PComparable.class, "stringAsComparable");
        match(PDate.class, "stringAsDate");
        match(PDateTime.class, "stringAsDateTime");
        match(PTime.class, "stringAsTime");
    }
}
