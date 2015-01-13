package com.querydsl.jpa.domain17;

import java.util.SortedSet;
import java.util.TreeSet;

public abstract class HidaBezGruppe<G extends HidaBezGruppe<G, B>, B extends HidaBez<B, G>> extends CapiBCKeyedByGrundstueck {

    Long id;

    SortedSet<B> bez = new TreeSet<B>();

}