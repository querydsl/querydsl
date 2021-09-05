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
package com.querydsl.osgi.itest.jpa.api.service;

import com.querydsl.osgi.itest.jpa.entity.Movie;
import java.util.List;

/**
 * Movie service api
 */
public interface MovieService {

  public Long addMovie(String name);

  public List<Movie> listAllMovies();

  public List<Movie> listMovies(String nameFilter);
}
