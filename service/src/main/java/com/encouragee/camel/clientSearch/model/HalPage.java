package com.encouragee.camel.clientSearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Helper class to (un)marshal hal+json page data
 */
@Data
@AllArgsConstructor
public class HalPage {

	private final PageMetadata page;

	@Data
	@Builder
	@AllArgsConstructor
	public static class PageMetadata {
		private final long size;
		private final long totalElements;
		private final long totalPages;
		private final long number;
		private final String cursorMark;
	}
}
