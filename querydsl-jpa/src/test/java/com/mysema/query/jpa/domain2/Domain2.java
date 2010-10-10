package com.mysema.query.jpa.domain2;

import java.util.Arrays;
import java.util.List;

public final class Domain2 {
    
    private Domain2(){}

    public static final List<Class<?>> classes = Arrays.<Class<?>>asList(
            Category.class, 
            CategoryProp.class, 
            Contact.class,
            Document2.class, 
            DocumentProp.class,
            User2.class, 
            UserProp.class);

}
