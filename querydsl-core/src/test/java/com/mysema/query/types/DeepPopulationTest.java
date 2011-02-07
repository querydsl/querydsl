package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.mysema.query.Tuple;
import com.mysema.query.types.path.StringPath;

public class DeepPopulationTest {

    public static class Entity1 {

        private Entity2 entity2;

        public Entity2 getEntity2() {
            return entity2;
        }

        public void setEntity2(Entity2 entity2) {
            this.entity2 = entity2;
        }

    }

    public static class Entity2 {

        private String name, id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @Test
    public void Deep_Population_Via_QBean(){
        StringPath name = new StringPath("name");
        StringPath id = new StringPath("id");
        QBean<Entity2> entity2Bean = new QBean<Entity2>(Entity2.class, name, id);
        QBean<Entity1> entity1Bean = new QBean<Entity1>(Entity1.class, Collections.singletonMap("entity2", entity2Bean));

        Entity1 entity1 = entity1Bean.newInstance("nameX","idX");
        assertEquals("nameX", entity1.getEntity2().getName());
        assertEquals("idX", entity1.getEntity2().getId());
    }

    @Test
    public void Deep_Population_Via_QTuple(){
        StringPath name = new StringPath("name");
        StringPath id = new StringPath("id");
        QBean<Entity2> entity2Bean = new QBean<Entity2>(Entity2.class, name, id);
        QTuple tupleExpr = new QTuple(entity2Bean);

        Tuple tuple = tupleExpr.newInstance("nameX","idX");
        assertEquals("nameX", tuple.get(entity2Bean).getName());
        assertEquals("idX", tuple.get(entity2Bean).getId());
    }

}
