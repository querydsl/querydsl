package com.mysema.query.domain;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic2Test {

    public static class Range<T extends Comparable<? super T>> {
        private T min;
        private T max;

        public T getMin() {
            return min;
        }

        public void setMin(T min) {
            this.min = min;
        }

        public T getMax() {
            return max;
        }

        public void setMax(T max) {
            this.max = max;
        }
    }

    @MappedSuperclass
    public static abstract class BaseEntity<T extends Comparable<? super T>> implements
            Serializable {
        @Embedded
        private Range<T> range;

        public Range<T> getRange() {
            return range;
        }

        public void setRange(Range<T> range) {
            this.range = range;
        }
    }

    @Entity
    public static class Foo extends BaseEntity<Integer> {
        
    }
    
    @Test
    public void test() {
        
    }

}
