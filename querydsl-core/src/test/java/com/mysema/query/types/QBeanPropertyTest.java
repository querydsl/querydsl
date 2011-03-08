package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.PathBuilder;

public class QBeanPropertyTest {

    public static class Entity {

        private Integer cId;

        private Integer eId;

        public Integer getcId() {
            return cId;
        }

        public void setcId(Integer cId) {
            this.cId = cId;
        }

        public Integer geteId() {
            return eId;
        }

        public void seteId(Integer eId) {
            this.eId = eId;
        }

    }

    @Test
    public void Field_Access(){
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        Entity bean = beanProjection.newInstance(1,2);
        assertEquals(Integer.valueOf(1), bean.getcId());
        assertEquals(Integer.valueOf(2), bean.geteId());
    }

    @Test
    public void Property_Access(){
        PathBuilder<Entity> entity = new PathBuilder<Entity>(Entity.class, "entity");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class,
                entity.getNumber("cId",Integer.class),
                entity.getNumber("eId", Integer.class));

        Entity bean = beanProjection.newInstance(1,2);
        assertEquals(Integer.valueOf(1), bean.getcId());
        assertEquals(Integer.valueOf(2), bean.geteId());
    }

}
