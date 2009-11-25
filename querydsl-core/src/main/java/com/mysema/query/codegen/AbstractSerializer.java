/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * AbstractSerializer is abstract base class for Serializer implementations
 * 
 * @author tiwe
 *
 */
public abstract class AbstractSerializer implements Serializer{

    @Override
    public String getQueryType(TypeModel type, EntityModel model, boolean raw){
        String localGenericName = type.getLocalGenericName(model, true);
        
        switch(type.getCategory()){
        case STRING:     
            return "PString";
        case BOOLEAN:    
            return "PBoolean";         
        case COMPARABLE: 
            return raw ? "PComparable" : "PComparable<" + localGenericName + ">";  
        case DATE:       
            return raw ? "PDate" : "PDate<" +localGenericName + ">"; 
        case DATETIME:   
            return raw ? "PDateTime" : "PDateTime<" + localGenericName + ">"; 
        case TIME:       
            return raw ? "PTime" : "PTime<" + localGenericName + ">"; 
        case NUMERIC:    
            return raw ? "PNumber" : "PNumber<" + localGenericName + ">";
        case ARRAY:
        case COLLECTION: 
        case SET:
        case LIST:
        case MAP:
        case SIMPLE:     
            return raw ? "PSimple" : "PSimple<" + localGenericName + ">";
        case ENTITY: 
//            String suffix = type.getLocalRawName(type).replace('.', '_');
            String suffix = type.getSimpleName();
            if (type.getPackageName().equals(model.getPackageName())){
                return model.getPrefix() + suffix;
            }else{
                return type.getPackageName() + "." + model.getPrefix() + suffix;
            } 
        }
        throw new IllegalArgumentException("Unsupported case " + type.getCategory());
    }
}
