package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.apt.domain.QGeneric12Test_ChannelRole;
import com.querydsl.apt.domain.QGeneric12Test_Permission;
import com.querydsl.apt.domain.QGeneric12Test_SubjectRole;

public class Generic12Test {

    @Entity
    @Inheritance
    @DiscriminatorColumn(name = "CONTEXT")
    public static abstract class Permission {
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
    public static abstract class Role<T extends Permission> {

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
    public static class SubjectRole extends Role { // missing type param, should be Role<SubjectPermission>
        // some constructors
    }

    @Test
    public void test() {
        Assert.assertEquals(QGeneric12Test_Permission.class,
                QGeneric12Test_ChannelRole.channelRole.permissions.get(0).getClass());
        Assert.assertEquals(QGeneric12Test_Permission.class,
                QGeneric12Test_SubjectRole.subjectRole.permissions.get(0).getClass());
    }

}
