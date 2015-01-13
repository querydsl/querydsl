package com.querydsl.jpa;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.path.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPerson is a Querydsl querydsl type for Person
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPerson extends EntityPathBase<Person> {

    private static final long serialVersionUID = -219463259L;

    public static final QPerson person = new QPerson("person");

    public final StringPath firstName = createString("firstName");

    public final StringPath lastName = createString("lastName");

    public QPerson(String variable) {
        super(Person.class, forVariable(variable));
    }

    public QPerson(Path<? extends Person> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPerson(PathMetadata<?> metadata) {
        super(Person.class, metadata);
    }

}

