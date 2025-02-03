package com.querydsl.core;

import com.querydsl.core.domain.QAnimal;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SimpleDTOProjection;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleDTOProjectionTest {

    public static class AnimalDTO {
        public AnimalDTO(boolean alive, double bodyWeight, Date dateField, int id, String name, Time timeField, int toes, int weight) {
            this.alive = alive;
            this.bodyWeight = bodyWeight;
            this.dateField = dateField;
            this.id = id;
            this.name = name;
            this.timeField = timeField;
            this.toes = toes;
            this.weight = weight;
        }

        private boolean alive;
        private double bodyWeight;
        private Date dateField;
        private int id;
        private String name;
        private Time timeField;
        private int toes;
        private int weight;

        // 기본 생성자 추가
        public AnimalDTO() {
        }

        // Getter 메서드
        public boolean isAlive() {
            return alive;
        }

        public double getBodyWeight() {
            return bodyWeight;
        }

        public Date getDateField() {
            return dateField;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Time getTimeField() {
            return timeField;
        }

        public int getToes() {
            return toes;
        }

        public int getWeight() {
            return weight;
        }

        // Setter 메서드
        public void setAlive(boolean alive) {
            this.alive = alive;
        }

        public void setBodyWeight(double bodyWeight) {
            this.bodyWeight = bodyWeight;
        }

        public void setDateField(Date dateField) {
            this.dateField = dateField;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTimeField(Time timeField) {
            this.timeField = timeField;
        }

        public void setToes(int toes) {
            this.toes = toes;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }


    @Test
    public void testSimpleDTOProjectionWithFields() {
        QAnimal animal = QAnimal.animal;

        // 1. Projections.fields() 방식으로 DTO 생성
        AnimalDTO dtoFromProjections = Projections.fields(
                AnimalDTO.class,
                animal.alive,
                animal.bodyWeight,
                animal.dateField,
                animal.id,
                animal.name,
                animal.timeField,
                animal.toes,
                animal.weight
        ).newInstance(
                true, // alive
                65.5, // bodyWeight
                new Date(System.currentTimeMillis()), // dateField
                1, // id
                "Lion", // name
                new Time(System.currentTimeMillis()), // timeField
                4, // toes
                150 // weight
        );

        // 2. SimpleDTOProjection.fields() 방식으로 DTO 생성
        AnimalDTO dtoFromSimpleDTOProjection = SimpleDTOProjection.fields(AnimalDTO.class, animal).newInstance(
                true, // alive
                65.5, // bodyWeight
                new Date(System.currentTimeMillis()), // dateField
                1, // id
                "Lion", // name
                new Time(System.currentTimeMillis()), // timeField
                4, // toes
                150 // weight
        );

        // 3. 두 DTO 비교
        assertNotNull(dtoFromProjections);
        assertNotNull(dtoFromSimpleDTOProjection);

        // 각 필드 값이 동일한지 비교
        assertEquals(dtoFromProjections.isAlive(), dtoFromSimpleDTOProjection.isAlive());
        assertEquals(dtoFromProjections.getBodyWeight(), dtoFromSimpleDTOProjection.getBodyWeight(), 0.001);
        assertEquals(dtoFromProjections.getDateField().getTime(), dtoFromSimpleDTOProjection.getDateField().getTime());
        assertEquals(dtoFromProjections.getId(), dtoFromSimpleDTOProjection.getId());
        assertEquals(dtoFromProjections.getName(), dtoFromSimpleDTOProjection.getName());
        assertEquals(dtoFromProjections.getTimeField().getTime(), dtoFromSimpleDTOProjection.getTimeField().getTime());
        assertEquals(dtoFromProjections.getToes(), dtoFromSimpleDTOProjection.getToes());
        assertEquals(dtoFromProjections.getWeight(), dtoFromSimpleDTOProjection.getWeight());
    }
}
