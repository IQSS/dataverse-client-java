package com.researchspace.dataverse.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.researchspace.dataverse.api.v1.SearchConfig;
import com.researchspace.dataverse.api.v1.SearchConfig.SearchConfigBuilder;
import com.researchspace.dataverse.api.v1.SearchOperations;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DataverseSearchHit;
import com.researchspace.dataverse.entities.SearchHit;
import com.researchspace.dataverse.entities.SearchResults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
@Slf4j
public class SearchOperationsImplV1 extends AbstractOpsImplV1 implements SearchOperations {

	@Override
	public SearchConfigBuilder builder() {
		return SearchConfig.builder();
	}

	@Override
	public void search(SearchConfig cfg) {
		String url = createV1Url("search");
		url = UriComponentsBuilder.fromUriString(url)
		 .queryParam("q", cfg.getQ())
		 .build(true)
		 .toUriString();
		RestTemplate template = createTemplate();
		HttpHeaders headers = addAPIKeyToHeader();
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ParameterizedTypeReference<DataverseResponse<SearchResults>> type = new ParameterizedTypeReference<DataverseResponse<SearchResults>>() {
		};
		
		ResponseEntity<DataverseResponse< SearchResults>> resp = template.exchange(url, HttpMethod.GET, entity, type);
		
		log.info(resp.getBody().getData().toString());

		
		
	}

	

}
