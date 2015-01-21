package com.querydsl.collections;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.BeanPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.StringPath;

public class QCar extends BeanPath<Car> {

    public final StringPath model = createString("model");

    public final NumberPath<Integer> horsePower = createNumber("horsePower", Integer.class);

    public final QPerson owner = new QPerson(new BeanPath<Person>(Person.class, this, "owner"));

    public static QCar car = new QCar(new BeanPath<Car>(Car.class, "car"));

    public QCar(BeanPath<? extends Car> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QCar(PathMetadata<?> metadata) {
        super(Car.class, metadata);
    }

}
