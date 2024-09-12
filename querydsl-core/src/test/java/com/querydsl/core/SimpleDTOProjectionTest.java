package com.querydsl.core;

import com.querydsl.core.domain.QAnimal;
import com.querydsl.core.types.SimpleDTOProjection;
import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleDTOProjectionTest {

    public static class AnimalDTO {
        private boolean alive;
        private double bodyWeight;
        private Date dateField;
        private int id;
        private String name;
        private Time timeField;
        private int toes;
        private int weight;

        public AnimalDTO(boolean alive, double bodyWeight, Date dateField,
                         int id, String name, Time timeField, int toes, int weight) {
            this.alive = alive;
            this.bodyWeight = bodyWeight;
            this.dateField = dateField;
            this.id = id;
            this.name = name;
            this.timeField = timeField;
            this.toes = toes;
            this.weight = weight;
        }

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
    }
    @Test
    public void testSimpleDTOProjection() {
        QAnimal animal = QAnimal.animal;
        SimpleDTOProjection<AnimalDTO> projection = new SimpleDTOProjection<>(AnimalDTO.class, animal);

        AnimalDTO dto = projection.newInstance(
                true, // alive
                65.5, // bodyWeight
                new Date(System.currentTimeMillis()), // dateField
                1, // id
                "Lion", // name
                new Time(System.currentTimeMillis()), // timeField
                4, // toes
                150 // weight
        );

        assertNotNull(dto);
        assertEquals(true, dto.isAlive());
        assertEquals(65.5, dto.getBodyWeight(), 0.001);
        assertEquals(new Date(System.currentTimeMillis()).getTime(), dto.getDateField().getTime());
        assertEquals(1, dto.getId());
        assertEquals("Lion", dto.getName());
        assertEquals(new Time(System.currentTimeMillis()).getTime(), dto.getTimeField().getTime());
        assertEquals(4, dto.getToes());
        assertEquals(150, dto.getWeight());
    }
}
