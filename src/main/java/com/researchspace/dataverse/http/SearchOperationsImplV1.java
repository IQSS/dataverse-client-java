package com.researchspace.dataverse.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.researchspace.dataverse.api.v1.SearchOperations;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchConfig.SearchConfigBuilder;
import com.researchspace.dataverse.search.entities.SearchResults;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SearchOperationsImplV1 extends AbstractOpsImplV1 implements SearchOperations {
	
	private SearchURLBuilder urlBuilder = new SearchURLBuilder();

	@Override
	public SearchConfigBuilder builder() {
		return SearchConfig.builder();		
	}

	@Override
	public DataverseResponse<SearchResults> search(SearchConfig cfg) {
		String url = createV1Url("search");
		url = urlBuilder.buildSearchUrl(url, cfg);
		RestTemplate template = createTemplate();
		HttpHeaders headers = addAPIKeyToHeader();
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ParameterizedTypeReference<DataverseResponse<SearchResults>> type = new ParameterizedTypeReference<DataverseResponse<SearchResults>>() {
		};		
		ResponseEntity<DataverseResponse< SearchResults>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		log.debug(resp.getBody().getData().toString());
		return resp.getBody();
	}
}
