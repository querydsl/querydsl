package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.path.PString;

public abstract class TestUtil {
    
    private TestUtil(){}
    
    public static Collection<EString> getStringProjections(PString path, PString otherPath, String knownValue){
        return Arrays.<EString>asList(
          path.add("Hello"),
          path.add(otherPath),
          path.concat("Hello"),
          path.concat(otherPath),
          path.lower(),
          path.stringValue(),
          path.substring(1),
          path.substring(0, 1),
          path.trim(),
          path.upper()
        );
    }
    
    public static Collection<EBoolean> getStringOperationFilters(PString path, PString otherPath, String knownValue){
        return Arrays.<EBoolean>asList(
                
        );
    }

}
