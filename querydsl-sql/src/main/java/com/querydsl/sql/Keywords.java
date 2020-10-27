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

import static com.google.common.collect.ImmutableSet.copyOf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

/**
 * Defines reserved keywords for the supported dialects
 */
final class Keywords {

    private Keywords() { }

    private static Set<String> readLines(String path) {
        try {
            return copyOf(Resources.readLines(
                    Keywords.class.getResource("/keywords/" + path), StandardCharsets.UTF_8,
                    new CommentDiscardingLineProcessor()));
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

    private static class CommentDiscardingLineProcessor implements LineProcessor<Collection<String>> {

        private final Collection<String> result = new HashSet<>();

        @Override
        public boolean processLine(String line) throws IOException {
            if (!line.isEmpty() && !line.startsWith("#")) {
                result.add(line);
            }
            return true;
        }

        @Override
        public Collection<String> getResult() {
            return result;
        }

    }
}
