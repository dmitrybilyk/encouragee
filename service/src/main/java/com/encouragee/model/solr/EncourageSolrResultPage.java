package com.encouragee.model.solr;

import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.SolrResultPage;

import java.util.List;

public class EncourageSolrResultPage<T> extends SolrResultPage<T> {

	@Getter
	private String cursorMark;

	public EncourageSolrResultPage(List<T> content) {
		super(content);
	}

	public EncourageSolrResultPage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total, null);
	}

	public EncourageSolrResultPage(List<T> content, Pageable pageable, long total, Float maxScore, String cursorMark) {
		super(content, pageable, total, maxScore);
		this.cursorMark = cursorMark;
	}


}
