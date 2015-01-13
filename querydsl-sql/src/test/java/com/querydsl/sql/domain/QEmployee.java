/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.sql.domain;

import java.math.BigDecimal;
import java.sql.Types;

import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.ForeignKey;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.path.TimePath;

//@Schema("PUBLIC")
//@Table("EMPLOYEE")
public class QEmployee extends RelationalPathBase<Employee> {

    private static final long serialVersionUID = 1394463749655231079L;

    public static final QEmployee employee = new QEmployee("EMPLOYEE");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath firstname = createString("firstname");

    public final StringPath lastname = createString("lastname");

    public final NumberPath<BigDecimal> salary = createNumber("salary", BigDecimal.class);

    public final DatePath<java.sql.Date> datefield = createDate("datefield", java.sql.Date.class);

    public final TimePath<java.sql.Time> timefield = createTime("timefield", java.sql.Time.class);

    public final NumberPath<Integer> superiorId = createNumber("superiorId", Integer.class);

    public final PrimaryKey<Employee> idKey = createPrimaryKey(id);

    public final ForeignKey<Employee> superiorIdKey = createForeignKey(superiorId, "ID");

    public final ForeignKey<Employee> _superiorIdKey = createInvForeignKey(id, "SUPERIOR_ID");

    public QEmployee(String path) {
        super(Employee.class, PathMetadataFactory.forVariable(path), "PUBLIC", "EMPLOYEE");
        addMetadata();
    }

    public QEmployee(PathMetadata<?> metadata) {
        super(Employee.class, metadata, "PUBLIC", "EMPLOYEE");
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(id, ColumnMetadata.named("ID").ofType(Types.INTEGER));
        addMetadata(firstname, ColumnMetadata.named("FIRSTNAME").ofType(Types.VARCHAR));
        addMetadata(lastname, ColumnMetadata.named("LASTNAME").ofType(Types.VARCHAR));
        addMetadata(salary, ColumnMetadata.named("SALARY").ofType(Types.DECIMAL));
        addMetadata(datefield, ColumnMetadata.named("DATEFIELD").ofType(Types.DATE));
        addMetadata(timefield, ColumnMetadata.named("TIMEFIELD").ofType(Types.TIME));
        addMetadata(superiorId, ColumnMetadata.named("SUPERIOR_ID").ofType(Types.INTEGER));
    }

}
