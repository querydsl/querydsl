package com.mysema.query.sql;

import java.util.regex.Pattern;

import com.mysema.query.codegen.EntityType;

/**
 * ExtendedNamingStrategy works like the DefaultNamingStrategy but tries to create foreign key property names in a different way.
 * 
 * It looks for patterns like this .*_&lt;forward&gt;_&lt;inverse&gt; and uses the forward part for the foreign key name and inverse
 * for the inverse foreign key name.
 * 
 * @author tiwe
 *
 */
public class ExtendedNamingStrategy extends DefaultNamingStrategy {
    
    private static final Pattern SPLIT = Pattern.compile("_");
    
    @Override
    public String getPropertyNameForForeignKey(String fkName, EntityType entityType) {
        String[] split = SPLIT.split(fkName);
        if (split.length > 2) {
            return getPropertyName(split[split.length-2], entityType);
        } else {
            return super.getPropertyNameForForeignKey(fkName, entityType);
        }
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String fkName, EntityType entityType) {
        String[] split = SPLIT.split(fkName);
        if (split.length > 2) {
            return getPropertyName(split[split.length-1], entityType);
        } else {
            return super.getPropertyNameForInverseForeignKey(fkName, entityType);
        }
    }
    
}
