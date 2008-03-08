package com.mysema.query.grammar.hql.domain2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * To set and get Content of html-pages with admin-gui.
 *
 */
@Entity
public class Content {

	@Id
	private String _title;

	@Column(length = 100000)
	private String _content;

	public Content() {
		//
	}

	/**
	 * @param title
	 * @param content
	 */
	public Content(String title, String content) {
		_title = title;
		_content = content;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return _content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content) {
		_content = content;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		_title = title;
	}

}
