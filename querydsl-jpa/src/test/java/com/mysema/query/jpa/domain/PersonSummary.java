package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class PersonSummary {

	private String name;

	private Long numberOfChildren;

	@QueryProjection
	public PersonSummary(String name, Long numberOfChildren) {
		super();
		this.name = name;
		this.numberOfChildren = numberOfChildren;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(Long numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

}
