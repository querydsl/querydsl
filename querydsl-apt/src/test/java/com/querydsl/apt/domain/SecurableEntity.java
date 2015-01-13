package com.querydsl.apt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * This is an example of using system ACL function. Note, field id is must,
 * abstract function getId must also implemented.
 */
@Entity
public class SecurableEntity extends AbstractSecurable<Long, Long> {

    private static final long serialVersionUID = 3197097608363811501L;

    @Id
    @TableGenerator(name = "SECUREENTITY_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "SECUREENTITY_SEQ")
    private Long securableEntityId;

    @Column(length = 50)
    private String name;

    // public Long getId() {
    // return getSecurableEntityId();
    // }

}