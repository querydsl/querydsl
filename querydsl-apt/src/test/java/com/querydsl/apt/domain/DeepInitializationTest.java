package com.querydsl.apt.domain;

import java.util.Collection;

import javax.persistence.*;

import org.junit.Test;

import com.querydsl.core.annotations.QueryInit;

public class DeepInitializationTest {

    @MappedSuperclass
    public abstract static class AbstractEntity implements Cloneable {

        @Id
        @Column(name = "ID")
        @GeneratedValue(generator = "SEQUENCE")
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

        private String number;

        public MyEmbeddable() {
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    @Test
    public void init_via_parent() {
        QDeepInitializationTest_Parent parent = QDeepInitializationTest_Parent.parent;
        parent.children.any().subChild.myEmbeddable.number.eq("Test");
    }

}
