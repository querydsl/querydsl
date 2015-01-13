package com.querydsl.apt.domain;
import javax.persistence.Entity;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity
public class Revision extends DefaultRevisionEntity {

    private static final long serialVersionUID = 4587663183059799464L;

}