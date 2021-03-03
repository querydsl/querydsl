/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Defines reserved keywords for the supported dialects
 */
final class Keywords {

    private Keywords() { }

    private static Set<String> readLines(String path) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Keywords.class.getResourceAsStream("/keywords/" + path)));) {
            return bufferedReader.lines()
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Set<String> DEFAULT = readLines("default");

    public static final Set<String> CUBRID = readLines("cubrid");
    public static final Set<String> DB2 = readLines("db2");
    public static final Set<String> DERBY = readLines("derby");
    public static final Set<String> FIREBIRD = readLines("firebird");
    public static final Set<String> H2 = readLines("h2");
    public static final Set<String> HSQLDB = readLines("hsqldb");
    public static final Set<String> MYSQL = readLines("mysql");
    public static final Set<String> ORACLE = readLines("oracle");
    public static final Set<String> POSTGRESQL = readLines("postgresql");
    public static final Set<String> SQLITE = readLines("sqlite");
    public static final Set<String> SQLSERVER2005 = readLines("sqlserver2005");
    public static final Set<String> SQLSERVER2008 = readLines("sqlserver2008");
    public static final Set<String> SQLSERVER2012 = readLines("sqlserver2012");

}
