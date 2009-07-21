/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.jdk5;

import com.mysema.query.annotations.Projection;
import com.mysema.query.annotations.Entity;

/**
 * Constants provides constants for use in Querydsl APT
 * 
 * @author tiwe
 * @version $Id$
 */
public final class Constants {
    private Constants() {
    }

    public static final String QD_ENTITY = Entity.class.getName();
    public static final String QD_DTO = Projection.class.getName();

    // JDO
    public static final String JDO_ENTITY = "javax.jdo.annotations.PersistenceCapable";

    // JPA
    public static final String JPA_SUPERCLASS = "javax.persistence.MappedSuperclass";
    public static final String JPA_ENTITY = "javax.persistence.Entity";
    public static final String JPA_EMBEDDABLE = "javax.persistence.Embeddable";

}
