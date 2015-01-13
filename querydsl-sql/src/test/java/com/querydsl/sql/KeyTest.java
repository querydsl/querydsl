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
package com.querydsl.sql;

import org.junit.Test;

import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.path.NumberPath;

@SuppressWarnings("serial")
public class KeyTest {

//    @Table("USER")
    public static class QUser extends RelationalPathBase<QUser>{

        public final NumberPath<Integer> id = createNumber("id", Integer.class);

        public final NumberPath<Integer> department = createNumber("department", Integer.class);

        public final NumberPath<Integer> superiorId = createNumber("superiorId", Integer.class);

        public final PrimaryKey<QUser> idKey = createPrimaryKey(id);

        public final ForeignKey<QDepartment> departmentKey = createForeignKey(department, "ID");

        public final ForeignKey<QUser> superiorIdKey = createForeignKey(superiorId, "ID");

        public QUser(String path) {
            super(QUser.class, PathMetadataFactory.forVariable(path), "", "USER");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(id, ColumnMetadata.named("ID"));
            addMetadata(department, ColumnMetadata.named("DEPARTMENT"));
            addMetadata(superiorId, ColumnMetadata.named("SUPERIOR_ID"));
        }

    }

//    @Table("DEPARTMENT")
    public static class QDepartment extends RelationalPathBase<QDepartment> {

        public final NumberPath<Integer> id = createNumber("id", Integer.class);

        public final NumberPath<Integer> company = createNumber("company", Integer.class);

        public final PrimaryKey<QDepartment> idKey = createPrimaryKey(id);

        public final ForeignKey<QCompany> companyKey = createForeignKey(company, "ID");

        public QDepartment(String path) {
            super(QDepartment.class, PathMetadataFactory.forVariable(path), "", "DEPARTMENT");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(id, ColumnMetadata.named("ID"));
            addMetadata(company, ColumnMetadata.named("COMPANY"));
        }

    }

//    @Table("COMPANY")
    public static class QCompany extends RelationalPathBase<QCompany> {

        public final NumberPath<Integer> id = createNumber("id", Integer.class);

        public final PrimaryKey<QCompany> idKey = createPrimaryKey(id);

        public QCompany(String path) {
            super(QCompany.class, PathMetadataFactory.forVariable(path), "", "COMPANY");
            addMetadata();
        }

        protected void addMetadata() {
            addMetadata(id, ColumnMetadata.named("ID"));
        }

    }

    @Test
    public void test() {
        QUser user = new QUser("user");
        QUser user2 = new QUser("user2");
        QDepartment department = new QDepartment("department");
        QCompany company = new QCompany("company");

        // superiorId -> id
        query().from(user).innerJoin(user.superiorIdKey, user2);

        // department -> id / company -> id
        query().from(user)
            .innerJoin(user.departmentKey, department)
            .innerJoin(department.companyKey, company);
    }

    private SQLQuery query() {
        return new SQLQuery(SQLTemplates.DEFAULT);
    }

}
