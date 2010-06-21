/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import org.junit.Test;

public class FinalPropertyTest {

    public static class Entity{

        private Entity2 property;

        public Entity2 getProperty() {
            return property;
        }

        public void setProperty(Entity2 property) {
            this.property = property;
        }

    }

    public static final class Entity2{

    }

    @Test
    public void test(){
        Entity entity = Alias.alias(Entity.class);
        Alias.$(entity.getProperty());
    }
}
