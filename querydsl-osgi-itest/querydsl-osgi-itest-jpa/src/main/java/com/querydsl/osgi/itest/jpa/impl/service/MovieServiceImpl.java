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
package com.querydsl.osgi.itest.jpa.impl.service;

import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.osgi.itest.jpa.api.service.MovieService;
import com.querydsl.osgi.itest.jpa.entity.Movie;
import com.querydsl.osgi.itest.jpa.entity.QMovie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Movie service implementation
 */
@Transactional
public class MovieServiceImpl implements MovieService {

  @PersistenceContext(unitName = "querydsl")
  private EntityManager entityManager;

  @Override
  public Long addMovie(String name) {
    Movie movie = new Movie();
    movie.setName(name);
    entityManager.persist(movie);
    return movie.getId();
  }

  @Override
  public List<Movie> listAllMovies() {
    QMovie qMovie = QMovie.movie;
    JPAQuery<Movie> query = new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager).selectFrom(qMovie);
    return query.fetch();
  }

  @Override
  public List<Movie> listMovies(String nameFilter) {
    QMovie qMovie = QMovie.movie;
    JPAQuery<Movie> query = new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager).selectFrom(qMovie);
    return query.where(qMovie.name.like(nameFilter + "%")).fetch();
  }

}
