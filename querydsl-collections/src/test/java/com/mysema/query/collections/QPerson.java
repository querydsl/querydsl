package com.mysema.query.collections;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.StringPath;

public class QPerson extends BeanPath<Person> {
    public final StringPath name = createString("name");
    
    public static QPerson car = new QPerson(new BeanPath<Person>(Person.class, "person"));

    public QPerson(BeanPath<? extends Person> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QPerson(PathMetadata<?> metadata) {
        super(Person.class, metadata);
    }

}
