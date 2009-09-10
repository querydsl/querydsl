/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;


/**
 * Serializers provides access to Serializer instances
 * 
 * @author tiwe
 *
 */
public final class Serializers {
    
    private Serializers(){}
    
    public static final Serializer ENTITY = new EntitySerializer(false);
    
    public static final Serializer SUPERTYPE = new SupertypeSerializer(false);
    
    public static final Serializer EMBEDDABLE = new EntitySerializer(true);
    
    public static final Serializer DTO = new DTOSerializer();    
}
