package com.querydsl.apt.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.domain.QQueryInit6Test_Content;

import org.hibernate.proxy.HibernateProxy;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class QueryInit6Test {

    @Entity(name = Component.NAME)
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public static abstract class Component implements Serializable {
        public static final String NAME = "Component";

        @Id
        protected String id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        private Component parent;

        @QueryType(PropertyType.ENTITY)
        @QueryInit("*")
        @Transient
        public Container getContainer(){
            Component temp = this.parent;
            if(this.parent instanceof HibernateProxy){
                temp = (Component) ((HibernateProxy) this.parent)
                        .getHibernateLazyInitializer().getImplementation();
            }

            if(temp instanceof Container)
                return (Container)temp;
            else{
                if (!temp.isRoot()) {
                    return temp.getParent().getContainer();
                } else {
                    return null;
                }
            }
        }

        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private Set<Component> children;

        protected Component() {

        }

        protected Component(String id, Component parent) {
            this.id = id;
            this.parent = parent;
            this.children = new HashSet<Component>();
        }

        @Transient
        public boolean isRoot() {
            return (parent == null);
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Set<Component> getChildren() {
            return children;
        }

        public void setChildren(Set<Component> children) {
            this.children = children;
        }

        public Component getParent() {
            return parent;
        }

        public void setParent(Component parent) {
            this.parent = parent;
        }
    }

    @Entity(name = Content.NAME)
    public static class Content extends Component {

        public static final String NAME = "Content";

        @Column(name = "quantity")
        private long quantity;

        public Content() {
            super(null, null);
        }

        public Content(String id, Component parent) {
            super(id, parent);
            this.quantity = 0;
        }

        @Override
        public String toString() {
            return "Content [id=" + id + " qty=" + quantity + "]";
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }
    }

    @Entity(name = Container.NAME)
    public static class Container extends Component {

        public static final String NAME = "Container";

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "packaging_id")
        private Packaging packaging;

        public Container() {
            super(null, null);
        }

        public Container(String id, Component parent) {
            super(id, parent);
            this.packaging = null;
        }

        public Packaging getPackaging() {
            return packaging;
        }

        public void setPackaging(Packaging packaging) {
            this.packaging = packaging;
        }
    }

    @Entity(name = Packaging.NAME)
    public static class Packaging implements Serializable {

        public static final String NAME = "Packaging";

        @Id
        private String id;

        @Column(name = "description")
        private String description;

        public Packaging() {

        }

        public Packaging(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    @Test
    public void test(){
        assertNotNull(QQueryInit6Test_Content.content.container.packaging);
        assertNotNull(QQueryInit6Test_Content.content.container.packaging.id);
    }
}
