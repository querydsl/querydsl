package com.querydsl.apt.domain.p10;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseReferencablePersistable<PK extends Serializable> extends BasePersistable<PK> {

}