package com.mysema.query.codegen;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;

public class QueryTypeFactoryImpl implements QueryTypeFactory {
    
    private final String prefix, suffix;
    
    @Inject
    public QueryTypeFactoryImpl(@Named("prefix") String prefix, @Named("suffix") String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    @Override
    public Type create(Type type){
        if (type.getPackageName().isEmpty()){
            return createWithoutPackage(type);
        }else{
            return createWithPackage(type);
        }
    }
    
    private Type createWithPackage(Type type){
        String packageName = type.getPackageName();
        String packagePrefix = packageName.startsWith("java") ? "ext." : "";
        String simpleName = prefix + normalizeName(type.getFullName().substring(packageName.length()+1)) + suffix;
        return new SimpleType(type.getCategory(), packagePrefix+packageName+"."+simpleName, packagePrefix+packageName, simpleName, false, false);
    }
    
    private Type createWithoutPackage(Type type){
        String simpleName = prefix + normalizeName(type.getFullName()) + suffix;
        return new SimpleType(type.getCategory(), simpleName, "", simpleName, false, false);
    }
    
    private String normalizeName(String name){
        return name.replace('.', '_').replace('$', '_');
    }

    
}
