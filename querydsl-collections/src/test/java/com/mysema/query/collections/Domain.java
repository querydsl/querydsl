/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.grammar.types.PathMetadata.forListAccess;
import static com.mysema.query.grammar.types.PathMetadata.forProperty;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.PathMetadata;

/**
 * Domain provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Domain {
    public static class Animal {
        boolean alive;
        java.util.Date birthdate;
        int bodyWeight, weight, toes;
        Color color;
        int id;    
        String name;
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
    }
    
    public static class Cat extends Animal{
        int breed;
        Color eyecolor;   
        List<Cat> kittens;
        Cat mate;
        public Cat() {            
        }
        public Cat(String name){
            this.kittens = Arrays.asList(new Cat());
            this.name = name;
            
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
        public Cat getMate() {
            return mate;
        }     
        public String toString(){
            return name;
        }
    }

    public enum Color {
        BLACK, TABBY
    }
    
    public static class QCat extends Path.PEntity<Cat>{
        public final Path.PBoolean alive = _boolean("alive");
        public final Path.PComparable<java.util.Date> birthdate = _comparable("birthdate",java.util.Date.class);
    
        public final Path.PNumber<java.lang.Integer> bodyWeight = _number("bodyWeight",java.lang.Integer.class);
        public final Path.PComparable<java.lang.Integer> breed = _comparable("breed",java.lang.Integer.class);
        public final Path.PSimple<Color> color = _simple("color",Color.class);
        public final Path.PSimple<Color> eyecolor = _simple("eyecolor",Color.class);
        public final Path.PNumber<java.lang.Integer> id = _number("id",java.lang.Integer.class);
        public final Path.PEntityList<Cat> kittens = _entitylist("kittens",Cat.class,"Cat");
        public QCat mate;
        public final Path.PString name = _string("name");
    
        public final Path.PComparable<java.lang.Integer> toes = _comparable("toes",java.lang.Integer.class);
        public final Path.PComparable<java.lang.Integer> weight = _comparable("weight",java.lang.Integer.class);
        public QCat(java.lang.String path) {
            super(Cat.class, "Cat", path);
            _mate();
        }
        public QCat(PathMetadata<?> metadata) {
            super(Cat.class, "Cat", metadata);
        }
        public QCat _mate() {
            if (mate == null) mate = new QCat(forProperty(this,"mate"));
            return mate;
        }
    
        public QCat kittens(Expr<Integer> index) {
            return new QCat(forListAccess(kittens,index));
        }
        public QCat kittens(int index) {
            return new QCat(forListAccess(kittens,index));
        } 
}
}
