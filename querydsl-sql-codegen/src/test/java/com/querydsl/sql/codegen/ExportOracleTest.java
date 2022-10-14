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
package com.querydsl.sql.codegen;

import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.Oracle;
import com.querydsl.sql.Connections;

@Category(Oracle.class)
public class ExportOracleTest extends ExportBaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TimeZone tz = TimeZone.getDefault();
        try {
            // change time zone to work around ORA-01882
            // see https://gist.github.com/jarek-przygodzki/cbea3cedae3aef2bbbe0ff6b057e8321
            // the test may work fine on your machine without this, but it fails when the GitHub runner executes it
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            Connections.initOracle();
        } finally {
            TimeZone.setDefault(tz);
        }
    }

    @Override
    public String getSchemaPattern() {
        return "QUERYDSL";
    }

}
