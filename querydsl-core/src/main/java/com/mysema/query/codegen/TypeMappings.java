/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.HashMap;
import java.util.Map;

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
 * @author tiwe
 *
 */
public class TypeMappings {

    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Custom>> customTypes = new HashMap<TypeCategory, Class<? extends Custom>>();

    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Expr>> exprTypes = new HashMap<TypeCategory, Class<? extends Expr>>();

    @SuppressWarnings("unchecked")
    private final Map<TypeCategory, Class<? extends Path>> pathTypes = new HashMap<TypeCategory, Class<? extends Path>>();

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

        register(TypeCategory.ENTITY, Expr.class, Path.class, CSimple.class);
    }

    public String getCustomType(Type type, EntityType model, boolean raw){
        return getCustomType(type, model, raw, false, false);
    }

    public String getCustomType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(customTypes, type, model, raw, rawParameters, extend);
    }

    public String getExprType(Type type, EntityType model, boolean raw){
        return getExprType(type, model, raw, false, false);
    }

    public String getExprType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(exprTypes, type, model, raw, rawParameters, extend);
    }

    public String getPathType(Type type, EntityType model, boolean raw){
        return getPathType(type, model, raw, false, false);
    }

    public String getPathType(Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        return getQueryType(pathTypes, type, model, raw, rawParameters, extend);
    }

    private String getQueryType(Map<TypeCategory, ? extends Class<?>> types, Type type, EntityType model, boolean raw, boolean rawParameters, boolean extend){
        String typeName = types.get(type.getCategory()).getSimpleName();
        return getQueryType(type, model, typeName, raw, rawParameters, extend);
    }

    public String getQueryType(Type type, EntityType model, String typeName, boolean raw, boolean rawParameters, boolean extend){
        String localName = null;
        TypeCategory category = type.getCategory();

        if (raw && category != TypeCategory.ENTITY){
            return typeName;
        }

        if (rawParameters){
            localName = type.getLocalRawName(model);
        }else{
            localName = type.getLocalGenericName(model, true);
        }
        if (!type.isFinal() && extend){
            localName = "? extends " + localName;
        }

        if (category == TypeCategory.STRING || category == TypeCategory.BOOLEAN){
            return typeName;

        }else if (category == TypeCategory.ENTITY){
            String suffix;
            if (!type.getPackageName().isEmpty()){
                suffix = type.getFullName().substring(type.getPackageName().length()+1).replace('.', '_');
            }else{
                suffix = type.getFullName().replace('.', '_');
            }
            if (type.getPackageName().equals(model.getPackageName()) || type.getPackageName().isEmpty()){
                return model.getPrefix() + suffix;
            }else{
                return type.getPackageName() + "." + model.getPrefix() + suffix;
            }

        }else{
            return typeName + "<" + localName + ">";
        }
    }

    @SuppressWarnings("unchecked")
    private void register(TypeCategory category,
            Class<? extends Expr> expr,
            Class<? extends Path> path,
            Class<? extends Custom> custom){
        exprTypes.put(category, expr);
        pathTypes.put(category, path);
        customTypes.put(category, custom);
    }

}
