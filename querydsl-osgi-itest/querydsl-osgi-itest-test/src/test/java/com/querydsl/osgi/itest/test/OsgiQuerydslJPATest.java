/*
 * Copyright 2021 Querydsl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.osgi.itest.test;

import com.querydsl.osgi.itest.jpa.api.service.MovieService;
import com.querydsl.osgi.itest.jpa.entity.Movie;
import java.util.List;
import org.apache.karaf.itests.KarafTestSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import static org.ops4j.pax.exam.OptionUtils.combine;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

/**
 * querydsl osgi test class
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OsgiQuerydslJPATest extends KarafTestSupport {

  @Configuration
  @Override
  public Option[] config() {
    Option[] origOptions = super.config();
    return combine(origOptions, new Option[]{
      CoreOptions.propagateSystemProperties("querydsl.version"),
      CoreOptions.mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.javax-inject").version("1_3")});
  }

  @Test
  public void testQuery() throws Exception {
    addFeaturesRepository("mvn:com.querydsl/querydsl-osgi-itest-jpa/" + System.getProperty("querydsl.version") + "/xml/features");
    installAndAssertFeature("query-dsl-osgi-itest-jpa");
    MovieService movieService = (MovieService) getOsgiService("com.querydsl.osgi.itest.jpa.api.service.MovieService", null, 20000L);
    assertTrue(movieService != null);
    List<Movie> ret1 = movieService.listAllMovies();
    assertEquals(0, ret1.size());
    movieService.addMovie("movieA");
    movieService.addMovie("movieB1");
    movieService.addMovie("movieB2");
    List<Movie> ret2 = movieService.listAllMovies();
    assertEquals(3, ret2.size());
    List<Movie> ret3 = movieService.listMovies("movieB");
    assertEquals(2, ret3.size());
  }

}
