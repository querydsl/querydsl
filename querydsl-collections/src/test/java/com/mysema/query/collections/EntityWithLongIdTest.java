package com.mysema.query.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
        ColQuery query = new ColQueryImpl().from(root, entities);
        query.where(root.id.eq(1000L));

        Long found = query.singleResult(root.id);
        Assert.assertNotNull(found);
        Assert.assertEquals(found.longValue(), 1000);
    }

}
