package com.researchspace.dataverse.http;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.EnumSet;

import org.springframework.web.util.UriComponentsBuilder;

import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchType;

class SearchURLBuilder {
	String buildSearchUrl(String path, SearchConfig cfg) {

		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(path).queryParam("q", cfg.getQ());
		EnumSet<SearchType> types = cfg.getType();
		if (types != null && !types.isEmpty()) {
			urlBuilder.queryParam("type", types.toArray());
		}
		if (!isEmpty(cfg.getSubtree())) {
			urlBuilder.queryParam("subtree", cfg.getSubtree());
		}
		if (!isEmpty(cfg.getFilterQuery())) {
			urlBuilder.queryParam("fq", cfg.getFilterQuery());
		}
		if (cfg.getSortBy() != null) {
			urlBuilder.queryParam("sort", cfg.getSortBy());
		}
		if (cfg.getSortOrder() != null) {
			urlBuilder.queryParam("order", cfg.getSortOrder());
		}
		if (cfg.getPerPage() != 0) {
			urlBuilder.queryParam("per_page", cfg.getPerPage());
		}
		if (cfg.getStart() != 0) {
			urlBuilder.queryParam("start", cfg.getStart());
		}
		if (cfg.isShowFacets()) {
			urlBuilder.queryParam("show_facets", true);
		}
		if (cfg.isShowRelevance()) {
			urlBuilder.queryParam("show_relevance", true);
		}
		path = urlBuilder.build(true).toUriString();
		return path;
	}

}
