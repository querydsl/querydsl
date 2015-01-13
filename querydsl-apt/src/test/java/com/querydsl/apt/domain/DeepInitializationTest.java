package com.querydsl.apt.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.junit.Test;

import com.querydsl.core.annotations.QueryInit;
import com.querydsl.apt.domain.QDeepInitializationTest_Parent;

public class DeepInitializationTest {

    @MappedSuperclass
    public static abstract class AbstractEntity implements Cloneable {

        @Id
        @Column(name = "ID")
        @GeneratedValue(generator="SEQUENCE")
        private long id;

        public long getId() {
            return id;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    @Entity
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "PARENT_SEQUENCE")
    public static class Parent extends AbstractEntity {
        @JoinColumn(name = "FK_PARENT_ID", nullable = false)
        @OneToMany(cascade = CascadeType.PERSIST)
        @QueryInit("subChild.*")
        private Collection<Child> children;

        public Parent() {
        }

        public Collection<Child> getChildren() {
            return children;
        }

        public void setChildren(Collection<Child> children) {
            this.children = children;
        }
    }

    @Entity
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "CHILD_SEQUENCE")
    public static class Child extends AbstractEntity {
        @OneToOne
        @JoinColumn(name = "FK_SUBCHILD_ID", referencedColumnName = "ID")
        private SubChild subChild;

        public Child() {
        }

        public SubChild getSubChild() {
            return subChild;
        }

        public void setSubChild(SubChild subChild) {
            this.subChild = subChild;
        }
    }

    @Entity
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "SUBCHILD_SEQUENCE")
    public static class SubChild extends AbstractEntity {
        @Embedded
        private MyEmbeddable myEmbeddable;

        public SubChild() {
        }

        public MyEmbeddable getMyEmbeddable() {
            return myEmbeddable;
        }

        public void setMyEmbeddable(MyEmbeddable myEmbeddable) {
            this.myEmbeddable = myEmbeddable;
        }
    }

    @Embeddable
    public static class MyEmbeddable {
        
        private String nummer;

        public MyEmbeddable() {
        }

        public String getNummer() {
            return nummer;
        }

        public void setNummer(String nummer) {
            this.nummer = nummer;
        }
    }
    
    @Test
    public void Init_Via_Parent() {
        QDeepInitializationTest_Parent parent = QDeepInitializationTest_Parent.parent;
        parent.children.any().subChild.myEmbeddable.nummer.eq("Test");
    }
    
}
