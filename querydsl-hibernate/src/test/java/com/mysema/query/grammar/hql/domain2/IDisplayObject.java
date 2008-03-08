package com.mysema.query.grammar.hql.domain2;

public interface IDisplayObject {

	public String getDisplayObjectType();
	
	public enum DisplayObjectType{
		USER, THING;
	}
}
