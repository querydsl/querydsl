/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.types.path.PathMetadata.forListAccess;
import static com.mysema.query.types.path.PathMetadata.forProperty;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadata;

/**
 * Domain provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class Domain {
    public static class Animal {
        protected boolean alive;
        protected java.util.Date birthdate = new java.util.Date();
        protected int bodyWeight, weight, toes;
        protected Color color;
        protected int id;
        protected String name;

        public java.util.Date getBirthdate() {
            return birthdate;
        }

        public int getBodyWeight() {
            return bodyWeight;
        }

        public Color getColor() {
            return color;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getToes() {
            return toes;
        }

        public int getWeight() {
            return weight;
        }

        public boolean isAlive() {
            return alive;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public void setBirthdate(java.util.Date birthdate) {
            this.birthdate = birthdate;
        }

        public void setBodyWeight(int bodyWeight) {
            this.bodyWeight = bodyWeight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void setToes(int toes) {
            this.toes = toes;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class Cat extends Animal {
        private int breed;
        private Color eyecolor;
        private List<Cat> kittens;
        private Map<String, Cat> kittensByName;
        private Cat mate;

        public Cat() {
        }

        public Cat(String name) {
            this.kittens = Arrays.asList(new Cat());
            this.kittensByName = Collections.singletonMap("Kitty", kittens.get(0));
            this.name = name;
        }
        
        public Cat(String name, int id) {
            this(name);
            this.id = id;
        }

        public int getBreed() {
            return breed;
        }

        public Color getEyecolor() {
            return eyecolor;
        }

        public List<Cat> getKittens() {
            return kittens;
        }

        public Map<String, Cat> getKittensByName() {
            return kittensByName;
        }

        public Cat getMate() {
            return mate;
        }

        public String toString() {
            return name;
        }

        public void setBreed(int breed) {
            this.breed = breed;
        }

        public void setEyecolor(Color eyecolor) {
            this.eyecolor = eyecolor;
        }

        public void setKittens(List<Cat> kittens) {
            this.kittens = kittens;
        }

        public void setMate(Cat mate) {
            this.mate = mate;
        }

    }

    public enum Color {
        BLACK, TABBY
    }

    public static class QCat extends PEntity<Cat> {
        public final PBoolean alive = _boolean("alive");
        public final PComparable<java.util.Date> birthdate = _comparable(
                "birthdate", java.util.Date.class);

        public final PNumber<java.lang.Integer> bodyWeight = _number(
                "bodyWeight", java.lang.Integer.class);
        public final PComparable<java.lang.Integer> breed = _comparable(
                "breed", java.lang.Integer.class);
        public final PSimple<Color> color = _simple("color", Color.class);
        public final PSimple<Color> eyecolor = _simple("eyecolor", Color.class);
        public final PNumber<java.lang.Integer> id = _number("id",
                java.lang.Integer.class);
        public final PEntityList<Cat> kittens = _entitylist("kittens",
                Cat.class, "Cat");
        public final PEntityMap<String, Cat> kittensByName = _entitymap(
                "kittensByName", String.class, Cat.class, "Cat");
        public QCat mate;
        public final PString name = _string("name");

        public final PComparable<java.lang.Integer> toes = _comparable("toes",
                java.lang.Integer.class);
        public final PComparable<java.lang.Integer> weight = _comparable(
                "weight", java.lang.Integer.class);

        public QCat(java.lang.String path) {
            super(Cat.class, "Cat", path);
            _mate();
        }

        public QCat(PathMetadata<?> metadata) {
            super(Cat.class, "Cat", metadata);
        }

        public QCat _mate() {
            if (mate == null)
                mate = new QCat(forProperty(this, "mate"));
            return mate;
        }

        public QCat kittens(Expr<Integer> index) {
            return new QCat(forListAccess(kittens, index));
        }

        public QCat kittens(int index) {
            return new QCat(forListAccess(kittens, index));
        }
    }
}
