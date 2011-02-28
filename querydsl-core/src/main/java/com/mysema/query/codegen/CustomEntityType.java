package com.mysema.query.codegen;

import com.mysema.codegen.model.Type;

/**
 * @author tiwe
 *
 */
public final class CustomEntityType extends EntityType {
    
    private final String packageName, simpleName, fullName;
    
    public CustomEntityType(String prefix, String suffix, String packageName, String simpleName, Type type) {
        super(prefix, suffix, type);
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.fullName = packageName + "." + simpleName;
    }

    @Override
    public String getFullName(){
        return fullName;
    }

    @Override
    public String getPackageName(){
        return packageName;
    }

    @Override
    public String getSimpleName(){
        return simpleName;
    }
}