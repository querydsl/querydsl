package com.mysema.query.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.types.path.NumberPath;

public class KeyAccessorsTest {

//    @Table("EMPLOYEE")
    public static class QEmployee extends RelationalPathBase<QEmployee> {

        private static final long serialVersionUID = 2020996035;

        public static final QEmployee employee = new QEmployee("EMPLOYEE");

        public class PrimaryKeys {

            public final PrimaryKey<QEmployee> sysIdx53 = createPrimaryKey(id);

        }

        public class ForeignKeys {

            public final ForeignKey<QEmployee> superiorFk = createForeignKey(superiorId, "ID");

            public final ForeignKey<QEmployee> _superiorFk = createInvForeignKey(id, "SUPERIOR_ID");

        }

        public final NumberPath<Integer> id = createNumber("ID", Integer.class);

        public final NumberPath<Integer> superiorId = createNumber("SUPERIOR_ID", Integer.class);

        public final PrimaryKeys pk = new PrimaryKeys();

        public final ForeignKeys fk = new ForeignKeys();

        public QEmployee(String variable) {
            super(QEmployee.class, forVariable(variable), null, "EMPLOYEE");
        }

    }

    @Test
    public void Keys(){
        assertNotNull(QEmployee.employee.pk.sysIdx53);
        assertNotNull(QEmployee.employee.fk.superiorFk);
        assertNotNull(QEmployee.employee.fk._superiorFk);
    }

}

