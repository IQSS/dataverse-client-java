package com.researchspace.dataverse.api.v1;

import java.util.EnumSet;

import org.apache.commons.lang.Validate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class SearchConfig {
	/**
	 * Builder for configuring search
	 * @author rspace
	 *
	 */
	public static class SearchConfigBuilder {
		/**
		 * Sets results per page. Maximum is 1000
		 * 
		 * @param perPage
		 *            if &gt 1000, will set to 1000
		 * @return
		 * @throws IllegalArgumentException
		 *             if <code>perPage</code> &lt= 0
		 */
		SearchConfigBuilder perPage(int perPage) {
			Validate.isTrue(perPage > 0, "Cannot have negative results per page");
			if (perPage > MAX_RESULTS_PER_PAGE) {
				perPage = MAX_RESULTS_PER_PAGE;
			}
			this.perPage = perPage;
			return this;
		}

		/**
		 * Sets results per page. Maximum is 1000
		 * 
		 * @param perPage
		 *            if &gt 1000, will set to 1000
		 * @return
		 * @throws IllegalArgumentException
		 *             if <code>perPage</code> &lt= 0
		 */
		SearchConfigBuilder start(int start) {
			Validate.isTrue(start > 0, "Cannot have negative starting point");
			this.start = start;
			return this;
		}
		
	}
	private static final int MAX_RESULTS_PER_PAGE = 1000;
	private EnumSet<SearchType> type;
	private String q, subtree, filterQuery;
	private SortBy sortBy;
	private SortOrder sortOrder;
	private int perPage, start;
	private boolean showRelevance, showFacets;

}
