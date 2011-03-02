package com.mysema.query.codegen;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;

public class QueryTypeFactory {

    private final String prefix, suffix;
    
    public QueryTypeFactory(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    public Type create(Type type){
        String packageName = type.getPackageName();
        TypeCategory category = type.getCategory();
        if (type.getPackageName().isEmpty()){
            String simpleName = prefix + normalizeName(type.getFullName()) + suffix;
            return new SimpleType(category, simpleName, "", simpleName, false, false);
        }else{
            String simpleName = prefix + normalizeName(type.getFullName().substring(packageName.length()+1)) + suffix;
            return new SimpleType(category, packageName+"."+simpleName, packageName, simpleName, false, false);
        }
    }
    
    private String normalizeName(String name){
        return name.replace('.', '_').replace('$', '_');
    }

    
}
