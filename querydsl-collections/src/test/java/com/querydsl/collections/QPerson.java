package com.querydsl.collections;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.StringPath;

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
