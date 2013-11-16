package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RooEntitiesTest {

    @Test
    public void RooJpaEntity() {
        assertNotNull(QRooEntities_MyEntity.myEntity);
    }

    @Test
    public void RooJpaActiveRecord() {
        assertNotNull(QRooEntities_MyEntity2.myEntity2);
    }

}
