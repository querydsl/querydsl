package com.mysema.query.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Test;

import com.mysema.query.annotations.QueryProjection;

public class Temporal2Test {

    @Entity
    public static class Cheque {
        @Temporal(TemporalType.DATE)
        private Date dataVencimento;

        @Column(precision=15, scale=2)        
        private BigDecimal valor;

        @QueryProjection
        public Cheque(Date dataVencimento, BigDecimal valor) {
            this.dataVencimento = dataVencimento;
            this.valor = valor;
        }
        
    }
    
    @Test
    public void test() {
        
    }
    
}
