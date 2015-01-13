package com.querydsl.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class EntityWithLongIdTest {

    private List<EntityWithLongId> entities = Arrays.asList(
            new EntityWithLongId(999L),
            new EntityWithLongId(1000L),
            new EntityWithLongId(1001L),
            new EntityWithLongId(1003L)
    );

    @Test
    public void SimpleEquals() {
        QEntityWithLongId root = QEntityWithLongId.entityWithLongId;
        CollQuery query = new CollQuery().from(root, entities);
        query.where(root.id.eq(1000L));

        Long found = query.singleResult(root.id);
        assertNotNull(found);
        assertEquals(found.longValue(), 1000);
    }
    
    @Test
    public void CartesianEquals() {
        QEntityWithLongId root = new QEntityWithLongId("root1");
        QEntityWithLongId root2 = new QEntityWithLongId("root2");
        assertEquals(entities.size(), new CollQuery()
            .from(root, entities).from(root2, entities)
            .where(root2.id.eq(root.id))
            .count());
    }
    
    @Test
    public void CartesianPlus1() {
        QEntityWithLongId root = new QEntityWithLongId("root1");
        QEntityWithLongId root2 = new QEntityWithLongId("root2");
        assertEquals(2, new CollQuery()
            .from(root, entities).from(root2, entities)
            .where(root2.id.eq(root.id.add(1)))
            .count());
    }

}
