package com.researchspace.dataverse.http;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchResults;

public class SearchOpsMockServerTest  {
	
	class SearchOpsTss extends SearchOperationsImplV1 {
		protected ResponseEntity<DataverseResponse<SearchResults>> performRequest(String url, RestTemplate template,
				HttpEntity<String> entity, ParameterizedTypeReference<DataverseResponse<SearchResults>> type) {
			MockRestServiceServer server = MockRestServiceServer.bindTo(template).build();
			server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("http://anyDataverse.com/api/v1/search?q=trees"))
			  .andExpect(method(HttpMethod.GET))
		     .andRespond(MockRestResponseCreators.withSuccess("{ \"status\":\"OK\",\"data\":{\"q\":\"trees\",\"total_count\":4}}",
		    		 MediaType.APPLICATION_JSON));
			return super.performRequest(url, template, entity, type);
		}
	}

	@Before
	public void setUp() throws Exception {

	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSearch() throws MalformedURLException {
		SearchConfig scfg = SearchConfig.builder().q("trees").build();
		SearchOpsTss tss = new SearchOpsTss();
		DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
		tss.configure(cfg);
		DataverseResponse<SearchResults> resp = tss.search(scfg);
		assertNotNull(resp.getData());
		assertEquals("trees", resp.getData().getQ());
	}

}
