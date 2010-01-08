/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.custom.CBoolean;
import com.mysema.query.types.custom.CComparable;
import com.mysema.query.types.custom.CDate;
import com.mysema.query.types.custom.CDateTime;
import com.mysema.query.types.custom.CNumber;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.custom.CTime;
import com.mysema.query.types.custom.Custom;
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
    
    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Custom>> customType = new HashMap<TypeCategory, Class<? extends Custom>>();
    
    public AbstractSerializer(){
        register(TypeCategory.STRING, EString.class, PString.class, CString.class);
        register(TypeCategory.BOOLEAN, EBoolean.class, PBoolean.class, CBoolean.class);
        register(TypeCategory.COMPARABLE, EComparable.class, PComparable.class, CComparable.class);
        register(TypeCategory.DATE, EDate.class, PDate.class, CDate.class);
        register(TypeCategory.DATETIME, EDateTime.class, PDateTime.class, CDateTime.class);
        register(TypeCategory.TIME, ETime.class, PTime.class, CTime.class);
        register(TypeCategory.NUMERIC, ENumber.class, PNumber.class, CNumber.class);

        register(TypeCategory.ARRAY, Expr.class, PSimple.class, CSimple.class);
        register(TypeCategory.COLLECTION, Expr.class, PSimple.class, CSimple.class);
        register(TypeCategory.SET, Expr.class, PSimple.class, CSimple.class);
        register(TypeCategory.LIST, Expr.class, PSimple.class, CSimple.class);
        register(TypeCategory.MAP, Expr.class, PSimple.class, CSimple.class);
        register(TypeCategory.SIMPLE, Expr.class, PSimple.class, CSimple.class);
        
        register(TypeCategory.ENTITY, Expr.class, Path.class, CSimple.class);
    }
    

    @SuppressWarnings("unchecked")
    private void register(TypeCategory category, 
            Class<? extends Expr> expr, 
            Class<? extends Path> path,
            Class<? extends Custom> custom){
        exprType.put(category, expr);
        pathType.put(category, path);
        customType.put(category, custom);
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
    
    public String getCustomType(TypeModel type, EntityModel model, boolean raw){
        String typeName = customType.get(type.getCategory()).getSimpleName();
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
            String suffix = type.getFullName().substring(type.getPackageName().length()+1).replace('.', '_');            
            if (type.getPackageName().equals(model.getPackageName())){
                return model.getPrefix() + suffix;
            }else{
                return type.getPackageName() + "." + model.getPrefix() + suffix;
            } 
        }
        throw new IllegalArgumentException("Unsupported case " + type.getCategory());
    }
}
