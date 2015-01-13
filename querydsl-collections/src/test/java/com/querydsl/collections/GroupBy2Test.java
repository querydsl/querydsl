package com.querydsl.collections;

import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.map;
import static org.junit.Assert.assertEquals;
import com.querydsl.core.group.GroupBy.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.group.GroupBy;

public class GroupBy2Test {
    
//    select u1.id,u1.name,r1.id,r1.name,s1.name from users u1
//    join roles r1 on u1.role = r1.id
//    join security_groups s1 on r1.secgroup = s1.id
    
    @QueryEntity
    public static class User {
        public Long id;
        public String name;
        public List<Role> roles;
    }
    
    @QueryEntity
    public static class Role {
        public Long id;
        public String name;
        public List<SecurityGroup> groups;
    }
    
    @QueryEntity
    public static class SecurityGroup {
        public Long id;
        public String name;
        
        public SecurityGroup(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    
    public static class UserDto {
        public Long id;
        public String name;
        public List<Long> roleIds;
        public List<String> roleNames;
        public List<Long> secIds;
        
        @QueryProjection
        public UserDto(Long id, String name, List<Long> roleIds, List<String> roleNames, List<Long> secIds) {
            this.id = id;
            this.name = name;
            this.roleIds = roleIds;
            this.roleNames = roleNames;
            this.secIds = secIds;
        }
        
        @QueryProjection
        public UserDto(Long id, String name, Map<Long, String> roles, Map<Long, String> groups) {
            this.id = id;
            this.name = name;
            this.roleIds = Lists.newArrayList(roles.keySet());
            this.roleNames = Lists.newArrayList(roles.values());
            this.secIds = Lists.newArrayList(groups.keySet());
        }
    }
    
    private List<User> users;
    
    @Before
    public void setUp() {
        Role r1 = new Role();
        r1.id = 1l;
        r1.name = "User";
        r1.groups = Lists.newArrayList(new SecurityGroup(1l, "User 1"));
                
        Role r2 = new Role();
        r2.id = 2l;
        r2.name = null; // NOTE this is null on purpose
        r2.groups = Lists.newArrayList(new SecurityGroup(2l, "Admin 1"),
                                       new SecurityGroup(3l, "Admin 2"));
        
        User u1 = new User();
        u1.id = 3l;
        u1.name = "Bob";
        u1.roles = Lists.newArrayList(r1);
        
        User u2 = new User();
        u2.id = 32l;
        u2.name = "Ann";
        u2.roles = Lists.newArrayList(r1, r2);
        
        users = Lists.newArrayList(u1, u2);
    }
    
    @Test
    public void test() {                               
        QGroupBy2Test_User user = QGroupBy2Test_User.user;
        QGroupBy2Test_Role role = QGroupBy2Test_Role.role;
        QGroupBy2Test_SecurityGroup group = QGroupBy2Test_SecurityGroup.securityGroup;
        
        Map<Long, UserDto> userDtos = CollQueryFactory.from(user, users)
                .innerJoin(user.roles, role)
                .innerJoin(role.groups, group)
                .transform(GroupBy.groupBy(user.id)
                    .as(new QGroupBy2Test_UserDto(
                            user.id, 
                            user.name,
                            list(role.id), 
                            list(role.name), 
                            list(group.id))));
        
        UserDto dto1 = userDtos.get(3l);
        assertEquals(1, dto1.roleIds.size());
        assertEquals(1, dto1.roleNames.size());
        assertEquals(1, dto1.secIds.size());
        
        UserDto dto2 = userDtos.get(32l);
        assertEquals(3, dto2.roleIds.size());
        assertEquals(1, dto2.roleNames.size());
        assertEquals(3, dto2.secIds.size());        
    }
    
    @Test
    public void test2() {
        QGroupBy2Test_User user = QGroupBy2Test_User.user;
        QGroupBy2Test_Role role = QGroupBy2Test_Role.role;
        QGroupBy2Test_SecurityGroup group = QGroupBy2Test_SecurityGroup.securityGroup;
        
        Map<Long, UserDto> userDtos = CollQueryFactory.from(user, users)
                .innerJoin(user.roles, role)
                .innerJoin(role.groups, group)
                .transform(GroupBy.groupBy(user.id)
                    .as(new QGroupBy2Test_UserDto(
                            user.id,
                            user.name,
                            map(role.id, role.name),
                            map(group.id, group.name))));
        
        UserDto dto1 = userDtos.get(3l);
        assertEquals(1, dto1.roleIds.size());
        assertEquals(1, dto1.roleNames.size());
        assertEquals(1, dto1.secIds.size());
        
        UserDto dto2 = userDtos.get(32l);
        assertEquals(2, dto2.roleIds.size());
        assertEquals(2, dto2.roleNames.size());
        assertEquals(3, dto2.secIds.size());    
    }

}
