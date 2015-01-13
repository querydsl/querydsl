package com.querydsl.apt.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.apt.domain.QTemporal2Test_Cheque;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.NumberPath;

public class Temporal2Test {

    @Entity
    public static class Cheque {
        @Temporal(TemporalType.DATE)
        private Date dataVencimento;

        @Column(precision=15, scale=2)
        private BigDecimal valor;

        @QueryProjection
        public Cheque(Date param0, BigDecimal param1) {
            this.dataVencimento = param0;
            this.valor = param1;
        }

    }

    @Test
    public void test() {
        DatePath<Date> datePath = new DatePath<Date>(Date.class, "date");
        NumberPath<BigDecimal> numberPath = new NumberPath<BigDecimal>(BigDecimal.class, "num");
        new QTemporal2Test_Cheque(datePath, numberPath);
    }

}
