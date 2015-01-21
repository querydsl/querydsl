package com.querydsl.apt.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Ignore;

@Ignore
public class Enum4Test {

    @Entity
    public static class Product{
        @Id
        long objectId;

        ExportUnit  exportunit;

    }

    public enum ExportUnit {

        MINUTE(0, 1),
        DAY8HOURS(1, 480),        // 8 hours
        EURO(2, null),
        DAY4HOURS(3, 240),    // 4 hours
        FIVEMINUTE(4, 5),
        HOUR(5,60),
        KILOMETERS(6,null),
        PIECE(7,null),
        WEEK(8,null),
        MONTH(9,null),
        PERIOD(10,null),
        YEAR(11,null),
        DAY24HOURS(12,null),
        DAY12HOURS(13,null),
        HALFYEAR(14, null),
        QUARTER(15, null),
        NONE(16, null);

        private final int code;
        private final Integer factor; // to minutes

        private ExportUnit(int code, Integer factor) {
            this.code = code;
            this.factor = factor;
        }

        public static ExportUnit get(int code) {
            for (ExportUnit value : ExportUnit.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Illegal ExportUnit: " + code);
        }
    }


}
