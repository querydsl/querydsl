package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.*;

import org.junit.Test;

import com.google.common.collect.Lists;

public class Generic12Test {

    @Entity
    @Inheritance
    @DiscriminatorColumn(name = "CONTEXT")
    public abstract static class Permission {
        // some common stuff
    }

    @Entity
    @DiscriminatorValue("CHANNEL")
    public static class ChannelPermission extends Permission {
        // CP specific stuff
    }

    @Entity
    @DiscriminatorValue("SUBJECT")
    public static class SubjectPermission extends Permission {
        // SP specific stuff
    }

    // A bunch of role classes

    @Entity
    @Inheritance
    @DiscriminatorColumn(name = "CONTEXT")
    public abstract static class Role<T extends Permission> {

        @ManyToMany(targetEntity = Permission.class)
        private final List<T> permissions = Lists.newArrayList();

    }

    @Entity
    @DiscriminatorValue("CHANNEL")
    public static class ChannelRole extends Role<ChannelPermission> {
        // some constructors
    }

    @Entity
    @DiscriminatorValue("SUBJECT")
    @SuppressWarnings("rawtypes") //expected
    public static class SubjectRole extends Role { // missing type param, should be Role<SubjectPermission>
        // some constructors
    }

    @Test
    public void test() {
        assertEquals(QGeneric12Test_Permission.class,
                QGeneric12Test_ChannelRole.channelRole.permissions.get(0).getClass());
        assertEquals(QGeneric12Test_Permission.class,
                QGeneric12Test_SubjectRole.subjectRole.permissions.get(0).getClass());
    }

}
