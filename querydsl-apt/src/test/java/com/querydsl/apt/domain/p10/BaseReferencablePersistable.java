package com.querydsl.apt.domain.p10;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseReferencablePersistable<PK extends Serializable> extends BasePersistable<PK> {

}