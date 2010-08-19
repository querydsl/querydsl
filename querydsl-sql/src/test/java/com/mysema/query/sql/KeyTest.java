/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("serial")
public class KeyTest {

    @Table("USER")
    public static class QUser extends BeanPath<QUser> implements RelationalPath<QUser>{

        public final PNumber<Integer> id = createNumber("ID", Integer.class);

        public final PNumber<Integer> department = createNumber("DEPARTMENT", Integer.class);

        public final PNumber<Integer> superiorId = createNumber("SUPERIOR_ID", Integer.class);

        public final PrimaryKey<QUser> idKey = new PrimaryKey<QUser>(this, id);

        public final ForeignKey<QDepartment> departmentKey = new ForeignKey<QDepartment>(this, department, "ID");

        public final ForeignKey<QUser> superiorIdKey = new ForeignKey<QUser>(this, superiorId,"ID");

        public QUser(String path) {
            super(QUser.class, PathMetadataFactory.forVariable(path));
        }

        @Override
        public Collection<ForeignKey<?>> getForeignKeys() {
            return Arrays.<ForeignKey<?>>asList(departmentKey, superiorIdKey);
        }

        @Override
        public Collection<ForeignKey<?>> getInverseForeignKeys() {
            return Collections.emptyList();
        }

        @Override
        public PrimaryKey<QUser> getPrimaryKey() {
            return idKey;
        }

    }

    @Table("DEPARTMENT")
    public static class QDepartment extends BeanPath<QDepartment> implements RelationalPath<QDepartment>{

        public final PNumber<Integer> id = createNumber("ID", Integer.class);

        public final PNumber<Integer> company = createNumber("COMPANY", Integer.class);

        public final PrimaryKey<QDepartment> idKey = new PrimaryKey<QDepartment>(this, id);

        public final ForeignKey<QCompany> companyKey = new ForeignKey<QCompany>(this, company, "ID");

        public QDepartment(String path) {
            super(QDepartment.class, PathMetadataFactory.forVariable(path));
        }

        @Override
        public Collection<ForeignKey<?>> getForeignKeys() {
            return Arrays.<ForeignKey<?>>asList(companyKey);
        }

        @Override
        public Collection<ForeignKey<?>> getInverseForeignKeys() {
            return Collections.emptyList();
        }

        @Override
        public PrimaryKey<QDepartment> getPrimaryKey() {
            return idKey;
        }

    }

    @Table("COMPANY")
    public static class QCompany extends BeanPath<QCompany> implements RelationalPath<QCompany>{

        public final PNumber<Integer> id = createNumber("ID", Integer.class);

        public final PrimaryKey<QCompany> idKey = new PrimaryKey<QCompany>(this, id);

        public QCompany(String path) {
            super(QCompany.class, PathMetadataFactory.forVariable(path));
        }

        @Override
        public Collection<ForeignKey<?>> getForeignKeys() {
            return Collections.emptyList();
        }

        @Override
        public Collection<ForeignKey<?>> getInverseForeignKeys() {
            return Collections.emptyList();
        }

        @Override
        public PrimaryKey<QCompany> getPrimaryKey() {
            return idKey;
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
