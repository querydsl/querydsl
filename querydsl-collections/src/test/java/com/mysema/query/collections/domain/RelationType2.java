package com.mysema.query.collections.domain;

import java.util.List;

import com.mysema.query.annotations.Entity;

@Entity
public class RelationType2<D extends RelationType2<D>> {
    List<D> list;
}