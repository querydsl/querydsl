package com.mysema.query.domain;

import java.util.Set;

import javax.persistence.Entity;

@Entity
public class Location {

    public Set<Path> paths;
}
