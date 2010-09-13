/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("serial")
public class KeyTest {

    @Table("USER")
    public static class QUser extends RelationalPathBase<QUser>{

        public final NumberPath<Integer> id = createNumber("ID", Integer.class);

        public final NumberPath<Integer> department = createNumber("DEPARTMENT", Integer.class);

        public final NumberPath<Integer> superiorId = createNumber("SUPERIOR_ID", Integer.class);

        public final PrimaryKey<QUser> idKey = createPrimaryKey(id);

        public final ForeignKey<QDepartment> departmentKey = createForeignKey(department, "ID");

        public final ForeignKey<QUser> superiorIdKey = createForeignKey(superiorId,"ID");

        public QUser(String path) {
            super(QUser.class, PathMetadataFactory.forVariable(path));
        }

    }

    @Table("DEPARTMENT")
    public static class QDepartment extends RelationalPathBase<QDepartment> {

        public final NumberPath<Integer> id = createNumber("ID", Integer.class);

        public final NumberPath<Integer> company = createNumber("COMPANY", Integer.class);

        public final PrimaryKey<QDepartment> idKey = createPrimaryKey(id);

        public final ForeignKey<QCompany> companyKey = createForeignKey(company, "ID");

        public QDepartment(String path) {
            super(QDepartment.class, PathMetadataFactory.forVariable(path));
        }
        
    }

    @Table("COMPANY")
    public static class QCompany extends RelationalPathBase<QCompany> {

        public final NumberPath<Integer> id = createNumber("ID", Integer.class);

        public final PrimaryKey<QCompany> idKey = createPrimaryKey(id);

        public QCompany(String path) {
            super(QCompany.class, PathMetadataFactory.forVariable(path));
        }

    }

    @Test
    public void test(){
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

    private SQLQuery query(){
        return new SQLQueryImpl(SQLTemplates.DEFAULT);
    }

}
