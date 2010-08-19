/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.HashMap;
import java.util.Map;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.custom.CBoolean;
import com.mysema.query.types.custom.CComparable;
import com.mysema.query.types.custom.CDate;
import com.mysema.query.types.custom.CDateTime;
import com.mysema.query.types.custom.CNumber;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.custom.CString;
import com.mysema.query.types.custom.CTime;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;

/**
 * TypeMappings defines mappings from Java types to {@link Expr}, {@link Path} and {@link Custom} types
 * 
 * @author tiwe
 *
 */
public class TypeMappings {

    private final Map<TypeCategory, ClassType> customTypes = new HashMap<TypeCategory, ClassType>();

    private final Map<TypeCategory, ClassType> exprTypes = new HashMap<TypeCategory, ClassType>();

    private final Map<TypeCategory, ClassType> pathTypes = new HashMap<TypeCategory, ClassType>();

    public TypeMappings(){
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

        register(TypeCategory.CUSTOM, Expr.class, Path.class, CSimple.class);
        register(TypeCategory.ENTITY, Expr.class, Path.class, CSimple.class);
    }

    public Type getCustomType(Type type, EntityType model, boolean raw){
        return getCustomType(type, model, raw, false, false);
    }

    public Type getCustomType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(customTypes, type, model, raw, rawParameters, extend);
    }

    public Type getExprType(Type type, EntityType model, boolean raw){
        return getExprType(type, model, raw, false, false);
    }

    public Type getExprType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(exprTypes, type, model, raw, rawParameters, extend);
    }

    public Type getPathType(Type type, EntityType model, boolean raw){
        return getPathType(type, model, raw, false, false);
    }

    public Type getPathType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(pathTypes, type, model, raw, rawParameters, extend);
    }

    private Type getQueryType(Map<TypeCategory, ClassType> types, Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        Type exprType = types.get(type.getCategory());
        return getQueryType(type, model, exprType, raw, rawParameters, extend);
    }

    public Type getQueryType(Type type, EntityType model, Type exprType, boolean raw, boolean rawParameters, boolean extend){
        TypeCategory category = type.getCategory();
        if (raw && category != TypeCategory.ENTITY && category != TypeCategory.CUSTOM){
            return exprType;
            
        }else if (category == TypeCategory.STRING || category == TypeCategory.BOOLEAN){
            return exprType;

        }else if (category == TypeCategory.ENTITY || category == TypeCategory.CUSTOM){
            String packageName = type.getPackageName();
            String simpleName;
            if (type.getPackageName().isEmpty()){
                simpleName = model.getPrefix()+type.getFullName().replace('.', '_');
                return new SimpleType(category, simpleName, "", simpleName, false, false);
            }else{                
                simpleName = model.getPrefix()+type.getFullName().substring(packageName.length()+1).replace('.', '_');
                return new SimpleType(category, packageName+"."+simpleName, packageName, simpleName, false, false);
            }

        }else{    
            if (rawParameters){
                type = new SimpleType(type);
            }
            if (!type.isFinal() && extend){
                type = new TypeExtends(type);
            }
            return new SimpleType(exprType, type);
            
        }
    }
    
    @SuppressWarnings("unchecked")
    public void register(TypeCategory category,
            Class<? extends Expr> expr,
            Class<? extends Path> path,
            Class<? extends Custom> custom){
        exprTypes.put(category, new ClassType(expr));
        pathTypes.put(category, new ClassType(path));
        customTypes.put(category, new ClassType(custom));
    }

}
