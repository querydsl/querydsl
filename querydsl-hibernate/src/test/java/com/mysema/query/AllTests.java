package com.mysema.query;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.mysema.query.grammar.hql.FeaturesTest;
import com.mysema.query.grammar.hql.HqlIntegrationTest;
import com.mysema.query.grammar.hql.HqlParserTest;

/**
 * AllTests provides
 *
 * @author tiwe
 * @version $Id$
 */
@RunWith(Suite.class)
@SuiteClasses({FeaturesTest.class,HqlIntegrationTest.class,HqlParserTest.class}) 
public class AllTests {

}
