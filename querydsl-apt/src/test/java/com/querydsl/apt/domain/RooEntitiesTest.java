package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RooEntitiesTest {

    @Test
    public void rooJpaEntity() {
        assertNotNull(QRooEntities_MyEntity.myEntity);
    }

    @Test
    public void rooJpaActiveRecord() {
        assertNotNull(QRooEntities_MyEntity2.myEntity2);
    }

}
