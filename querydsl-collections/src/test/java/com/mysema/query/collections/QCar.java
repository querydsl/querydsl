package com.mysema.query.collections;

import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

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
