/*
 * Copyright 2014 Timo Westkämper.
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
package com.mysema.query;


import static org.junit.Assert.assertEquals;

import com.mysema.query.ddl.CreateTableClause;
import com.mysema.query.ddl.DropTableClause;
import com.mysema.query.sql.ColumnMetadata;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.StringPath;

import org.junit.*;

public class KeywordQuotingBase extends AbstractBaseTest {

    private static class Quoting extends RelationalPathBase<Quoting> {

        public static final Quoting quoting = new Quoting("quoting");

        public final StringPath from = createString("from");
        public final BooleanPath all = createBoolean("all");

        private Quoting(String path) {
            super(Quoting.class, PathMetadataFactory.forVariable(path), "PUBLIC", "quoting");
            addMetadata();
        }

        public Quoting(PathMetadata<?> metadata) {
            super(Quoting.class, metadata, "PUBLIC", "quoting");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(from, ColumnMetadata.named("from"));
            addMetadata(all, ColumnMetadata.named("all"));
        }
    }

    private final Quoting quoting = Quoting.quoting;

    @Before
    public void setUp() throws Exception {
        new CreateTableClause(connection, configuration, "quoting")
                .column("from", String.class).size(30)
                .column("all", Boolean.class)
                .execute();
        execute(insert(quoting)
                .columns(quoting.from, quoting.all)
                .values("from", true));
    }

    @After
    public void tearDown() throws Exception {
        new DropTableClause(connection, configuration, "quoting")
                .execute();
    }

    @Test
    public void Keywords() {
        Quoting from = new Quoting("from");
        assertEquals("from", query().from(quoting.as(from))
                .where(from.from.eq("from")
                        .and(from.all.isNotNull()))
                .singleResult(from.from));
    }

}
