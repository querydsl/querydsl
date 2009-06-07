/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.runner.RunWith;

import com.mysema.query.hql.Hibernate;
import com.mysema.query.hql.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@Hibernate(properties = "derby.properties")
public class DerbyStandardTest extends AbstractStandardTest{

}
