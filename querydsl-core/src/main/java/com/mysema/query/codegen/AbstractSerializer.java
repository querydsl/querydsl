/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;

/**
 * AbstractSerializer is abstract base class for Serializer implementations
 * 
 * @author tiwe
 *
 */
public abstract class AbstractSerializer implements Serializer{

    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Expr>> exprType = new HashMap<TypeCategory, Class<? extends Expr>>();
    
    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Path>> pathType = new HashMap<TypeCategory, Class<? extends Path>>();
    
    public AbstractSerializer(){
        pathType.put(TypeCategory.STRING, PString.class);
        pathType.put(TypeCategory.BOOLEAN, PBoolean.class);
        pathType.put(TypeCategory.COMPARABLE, PComparable.class);
        pathType.put(TypeCategory.DATE, PDate.class);
        pathType.put(TypeCategory.DATETIME, PDateTime.class);
        pathType.put(TypeCategory.TIME, PTime.class);
        pathType.put(TypeCategory.NUMERIC, PNumber.class);
        pathType.put(TypeCategory.ARRAY, PSimple.class);
        pathType.put(TypeCategory.COLLECTION, PSimple.class);
        pathType.put(TypeCategory.SET, PSimple.class);
        pathType.put(TypeCategory.LIST, PSimple.class);
        pathType.put(TypeCategory.MAP, PSimple.class);
        pathType.put(TypeCategory.SIMPLE, PSimple.class);
        pathType.put(TypeCategory.ENTITY, Path.class);
        
        exprType.put(TypeCategory.STRING, EString.class);
        exprType.put(TypeCategory.BOOLEAN, EBoolean.class);
        exprType.put(TypeCategory.COMPARABLE, EComparable.class);
        exprType.put(TypeCategory.DATE, EDate.class);
        exprType.put(TypeCategory.DATETIME, EDateTime.class);
        exprType.put(TypeCategory.TIME, ETime.class);
        exprType.put(TypeCategory.NUMERIC, ENumber.class);
        exprType.put(TypeCategory.ARRAY, Expr.class);
        exprType.put(TypeCategory.COLLECTION, Expr.class);
        exprType.put(TypeCategory.SET, Expr.class);
        exprType.put(TypeCategory.LIST, Expr.class);
        exprType.put(TypeCategory.MAP, Expr.class);
        exprType.put(TypeCategory.SIMPLE, Expr.class);
        exprType.put(TypeCategory.ENTITY, Expr.class);
    }
    
    @Override
    public String getPathType(TypeModel type, EntityModel model, boolean raw){
        String typeName = pathType.get(type.getCategory()).getSimpleName();
        return getQueryType(type, model, typeName, raw, false);
    }
    
    public String getExprType(TypeModel type, EntityModel model, boolean raw){
        String typeName = exprType.get(type.getCategory()).getSimpleName();
        return getQueryType(type, model, typeName, raw, true);
    }
    
    public String getQueryType(TypeModel type, EntityModel model, String typeName, boolean raw, boolean extend){
        String localGenericName = null;         
        
        if (raw && type.getCategory() != TypeCategory.ENTITY){
            return typeName;
        }else{
            localGenericName = type.getLocalGenericName(model, true);
            if (!type.isFinal() && extend){
                localGenericName = "? extends " + localGenericName;
            }
        }
        
        switch(type.getCategory()){
        case STRING:     
            return typeName;
        case BOOLEAN:    
            return typeName;         
        case COMPARABLE: 
            return typeName + "<" + localGenericName + ">";  
        case DATE:       
            return typeName + "<" +localGenericName + ">"; 
        case DATETIME:   
            return typeName + "<" + localGenericName + ">"; 
        case TIME:       
            return typeName + "<" + localGenericName + ">"; 
        case NUMERIC:    
            return typeName + "<" + localGenericName + ">";
        case ARRAY:
        case COLLECTION: 
        case SET:
        case LIST:
        case MAP:
        case SIMPLE:     
            return typeName + "<" + localGenericName + ">";
        case ENTITY: 
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
