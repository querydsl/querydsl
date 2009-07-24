package com.mysema.query;

import org.junit.runner.RunWith;

@RunWith(HibernateTestRunner.class)
@Hibernate(properties = "mysql.properties")
public abstract class MySQLStandardTest extends AbstractStandardTest{
//    0 failures
    
//    5 errors
//    cast(cat.id,class java.lang.Byte) failed : could not execute query
//    cast(cat.id,class java.lang.Double) failed : could not execute query
//    cast(cat.id,class java.lang.Float) failed : could not execute query
//    cast(cat.id,class java.lang.Long) failed : could not execute query
//    cast(cat.id,class java.lang.Short) failed : could not execute query
}
