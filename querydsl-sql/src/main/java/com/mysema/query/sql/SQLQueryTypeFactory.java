package com.mysema.query.sql;

import javax.inject.Inject;
import javax.inject.Named;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.query.codegen.QueryTypeFactory;

public final class SQLQueryTypeFactory implements QueryTypeFactory{

    private final int stripStart, stripEnd;

    private final String prefix, suffix, packageName;

    @Inject
    public SQLQueryTypeFactory(
            @Named(SQLCodegenModule.BEAN_PREFIX) String beanPrefix,
            @Named(SQLCodegenModule.BEAN_SUFFIX) String beanSuffix,
            @Named(SQLCodegenModule.PREFIX) String prefix,
            @Named(SQLCodegenModule.SUFFIX) String suffix,
            @Named(SQLCodegenModule.PACKAGE_NAME) String packageName) {
        this.stripStart = beanPrefix.length();
        this.stripEnd = beanSuffix.length();
        this.prefix = prefix;
        this.suffix = suffix;
        this.packageName = packageName;
    }

    @Override
    public Type create(Type type){
        String simpleName = type.getSimpleName();
        simpleName = prefix + simpleName.substring(stripStart, simpleName.length()-stripEnd) + suffix;
        return new SimpleType(packageName + "." + simpleName, packageName, simpleName);
    }


}
