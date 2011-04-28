package com.mysema.query.jpa.hibernate.sql;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.Table;
import com.mysema.query.types.path.NumberPath;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@Ignore
@RunWith(HibernateTestRunner.class)
@HibernateConfig("mysql.properties")
public class MySQLSQLTest extends AbstractSQLTest {
    
    @Table("Animal")
    public class SAnimal extends RelationalPathBase<SAnimal> {

        private static final long serialVersionUID = 1L;
        
        public final NumberPath<Integer> id = createNumber("id", Integer.class);
        
        public SAnimal(String variable) {
            super(SAnimal.class, forVariable(variable));
        }

    }       

    @Override
    @Test
    public void Count() {
        query().from(new SAnimal("cat")).count();        
        query().from(new SAnimal("cat")).countDistinct();
    }
    
}

