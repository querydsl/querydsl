package com.mysema.query.sql;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.query.codegen.QueryTypeFactory;

public class SQLQueryTypeFactory implements QueryTypeFactory{
    
    private int stripStart, stripEnd;
    
    private final String prefix, suffix, packageName;
    
    @Inject
    public SQLQueryTypeFactory(
            @Named("beanPrefix") String beanPrefix, 
            @Named("beanSuffix") String beanSuffix,
            @Named("prefix") String prefix, 
            @Named("suffix") String suffix, 
            @Named("packageName") String packageName) {
        this.stripStart = beanPrefix.length();
        this.stripEnd = beanSuffix.length();
        this.prefix = prefix;
        this.suffix = suffix;
        this.packageName = packageName;
    }
    
    @Override
    public Type create(Type type){
        String simpleName = prefix + type.getSimpleName().substring(stripStart, type.getSimpleName().length()-stripEnd) + suffix;
        return new SimpleType(packageName + "." + simpleName, packageName, simpleName);
    }
    

}
