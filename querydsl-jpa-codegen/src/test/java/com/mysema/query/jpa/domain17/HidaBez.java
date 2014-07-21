package com.mysema.query.jpa.domain17;

public abstract class HidaBez<B extends HidaBez<B, G>, G extends HidaBezGruppe<G, B>> extends CapiBCKeyedByGrundstueck {

    Long id;

}