/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import com.mysema.query.annotations.DTO;
import com.mysema.query.annotations.Entity;

/**
 * Constants provides constants for use in Querydsl APT
 *
 * @author tiwe
 * @version $Id$
 */
public interface Constants {
    String qdEntity= Entity.class.getName();
    String qdDto = DTO.class.getName();
    
    String jpaSuperClass = "javax.persistence.MappedSuperclass";
    String jpaEntity = "javax.persistence.Entity";                
    String jpaEmbeddable = "javax.persistence.Embeddable";

}
