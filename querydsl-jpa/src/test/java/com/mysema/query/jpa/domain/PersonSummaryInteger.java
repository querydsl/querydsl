package com.mysema.query.jpa.domain;

import com.mysema.query.annotations.QueryProjection;

public class PersonSummaryInteger {

	private String name;

	private Integer numberOfChildren;

	@QueryProjection
	public PersonSummaryInteger(String name, Integer numberOfChildren) {
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

	public Integer getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(Integer numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

}
