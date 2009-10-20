package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PNumber;


public class InterfaceTypeTest {
    
    @QueryEntity
    public interface InterfaceType {
        InterfaceType getRelation();

        List<InterfaceType> getRelation2();

        List<? extends InterfaceType> getRelation3();

        int getRelation4();
        
        String getProp();
    }
    
    @QueryEntity
    public interface InterfaceType2 {

        public String getProp2();
        
    }
    
    @QueryEntity
    public interface InterfaceType3 extends InterfaceType, InterfaceType2{
        
        public String getProp3();

    }
    
    @QueryEntity
    public interface InterfaceType4{
        
        public String getProp4();

    }
    
    @QueryEntity    
    public interface InterfaceType5 extends InterfaceType3, InterfaceType4{
        
        public String getProp5();
        
    }
    
    @Test
    public void QInterfaceType_reation() throws SecurityException, NoSuchFieldException{
        assertEquals(QInterfaceType.class, QInterfaceType.class.getField("relation").getType());
    }
    
    @Test
    public void QInterfaceType_reation2() throws SecurityException, NoSuchFieldException{
        assertEquals(PEntityList.class,    QInterfaceType.class.getField("relation2").getType());
    }
    
    @Test
    public void QInterfaceType_reation3() throws SecurityException, NoSuchFieldException{
        assertEquals(PEntityList.class,    QInterfaceType.class.getField("relation3").getType());
    }
    
    @Test
    public void QInterfaceType_reation4() throws SecurityException, NoSuchFieldException{
        assertEquals(PNumber.class,     QInterfaceType.class.getField("relation4").getType());
    }
    
    @Test
    public void testQInterfaceType3() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceType3.class;
        cl.getField("prop");
        cl.getField("prop2");
        cl.getField("prop3");
    }
    
    @Test
    public void testQInterfaceType5() throws SecurityException, NoSuchFieldException{
        Class<?> cl = QInterfaceType5.class;
        cl.getField("prop");
        cl.getField("prop2");
        cl.getField("prop3");
        cl.getField("prop4");
        cl.getField("prop5");
    }
    

}
