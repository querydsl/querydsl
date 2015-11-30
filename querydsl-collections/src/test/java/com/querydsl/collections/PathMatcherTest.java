package com.querydsl.collections;

import static com.querydsl.collections.PathMatcher.hasValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Test;

public class PathMatcherTest {

    private static final QCar $ = QCar.car;

    @Test
    public void match() {
        Car car = new Car();
        car.setHorsePower(123);

        assertThat(car, hasValue($.horsePower));
        assertThat(car, hasValue($.horsePower, equalTo(123)));
    }

    @Test
    public void mismatch() {
        Car car = new Car();
        car.setHorsePower(123);

        Description mismatchDescription = new StringDescription();
        hasValue($.horsePower, equalTo(321)).describeMismatch(car, mismatchDescription);
        assertEquals("value \"car.horsePower\" was <123>", mismatchDescription.toString());
    }

    @Test
    public void describe() {
        Description description = new StringDescription();
        hasValue($.horsePower, equalTo(321)).describeTo(description);
        assertEquals("valueOf(\"car.horsePower\", <321>)", description.toString());
    }

}
