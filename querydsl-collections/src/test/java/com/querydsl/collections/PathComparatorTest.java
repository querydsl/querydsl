package com.querydsl.collections;

import static com.querydsl.collections.PathComparator.pathComparator;
import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

public class PathComparatorTest {

    private Comparator<Car> comparator;
    
    @Before
    public void setUpComparator() {
        comparator = pathComparator(QCar.car.horsePower);
    }

    @Test
    public void EqualReference() {
        Car car = new Car();
        assertEquals(0, comparator.compare(car, car));
    }
    
    @Test
    public void SemanticallyEqual() {
        Car car = new Car();
        car.setModel("car");
        car.setHorsePower(50);
        
        Car similarCar = new Car();
        similarCar.setModel("car");
        similarCar.setHorsePower(50);
        
        assertEquals(0, comparator.compare(car, similarCar));
    }
    
    @Test
    public void LeftIsNull() {
        assertEquals(-1, comparator.compare(null, new Car()));
    }
    
    @Test
    public void RightIsNull() {
        assertEquals(1, comparator.compare(new Car(), null));
    }
    
    @Test
    public void CompareOnValue() {
        Car car = new Car();
        car.setHorsePower(50);
        
        Car betterCar = new Car();
        betterCar.setHorsePower(150);
        
        assertEquals(-1, comparator.compare(car, betterCar));
    }
    
}
