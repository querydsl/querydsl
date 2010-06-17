package com.mysema.query.sql;

import org.junit.Test;

import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PathMetadataFactory;

@SuppressWarnings("serial")
public class KeyTest {
    
    @Table("USER")
    public static class QUser extends PEntity<QUser>{

        public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);
        
        public final PNumber<java.lang.Integer> department = createNumber("DEPARTMENT", java.lang.Integer.class);
        
        public final PNumber<java.lang.Integer> superiorId = createNumber("SUPERIOR_ID", java.lang.Integer.class);
                
        private final PrimaryKey<QUser,Integer> idKey = new PrimaryKey<QUser,Integer>(this, id);
        
        private final ForeignKey<QDepartment,Integer> departmentKey = new ForeignKey<QDepartment,Integer>(this, department);
                
        private final ForeignKey<QUser,Integer> superiorIdKey = new ForeignKey<QUser,Integer>(this, superiorId);
        
        public QUser(String path) {
            super(QUser.class, PathMetadataFactory.forVariable(path));
        }
        
        public PrimaryKey<QUser, Integer> id() {
            return idKey;
        }

        public ForeignKey<QDepartment, Integer> department() {
            return departmentKey;
        }
        
        public ForeignKey<QUser, Integer> superiorId() {
            return superiorIdKey;
        }
    }
    
    @Table("DEPARTMENT")
    public static class QDepartment extends PEntity<QDepartment>{

        public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);
        
        public final PNumber<java.lang.Integer> company = createNumber("COMPANY", java.lang.Integer.class);
        
        private final PrimaryKey<QDepartment,Integer> idKey = new PrimaryKey<QDepartment,Integer>(this, id);
        
        private final ForeignKey<QCompany,Integer> companyKey = new ForeignKey<QCompany,Integer>(this, company);
        
        public QDepartment(String path) {
            super(QDepartment.class, PathMetadataFactory.forVariable(path));
        }
        
        public PrimaryKey<QDepartment, Integer> id() {
            return idKey;
        }

        public ForeignKey<QCompany, Integer> company() {
            return companyKey;
        }
        
        
    }
    
    @Table("COMPANY")
    public static class QCompany extends PEntity<QCompany>{
        
        public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);

        private final PrimaryKey<QCompany,Integer> idKey = new PrimaryKey<QCompany,Integer>(this, id);
        
        public QCompany(String path) {
            super(QCompany.class, PathMetadataFactory.forVariable(path));
        }
        
        public PrimaryKey<QCompany, Integer> id() {
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
        query().from(user).innerJoin(user.superiorId(), user.id());
        
        // superiorId -> superiorId
        query().from(user).innerJoin(user.superiorId(), user2.superiorId());
        
        // department -> id / company -> id
        query().from(user)
            .innerJoin(user.department(), department.id())
            .innerJoin(department.company(), company.id());
    }
    
    private SQLQuery query(){
        return new SQLQueryImpl(SQLTemplates.DEFAULT);
    }
    
}
