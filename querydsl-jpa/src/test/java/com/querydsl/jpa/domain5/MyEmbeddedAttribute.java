package com.querydsl.jpa.domain5;

import javax.persistence.*;

@Embeddable
public class MyEmbeddedAttribute {
    @ManyToOne
    private MyOtherEntity attributeWithInitProblem;
}