package com.querydsl.jpa.suites;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Teradata {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("teradata");
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                entityManager.getTransaction().commit();
            } finally {
                entityManager.close();
            }
        } finally {
            entityManagerFactory.close();
        }
    }

}
