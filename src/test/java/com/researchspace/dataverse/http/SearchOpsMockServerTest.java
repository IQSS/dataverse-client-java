package com.researchspace.dataverse.http;

import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
import com.researchspace.dataverse.search.entities.SearchType;
import com.researchspace.dataverse.testutils.TestFileUtils;

public class SearchOpsMockServerTest  {
		
	@Before
	public void setUp() throws Exception {
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimpleSearch() throws MalformedURLException {
		SearchConfig scfg = SearchConfig.builder().q("trees").build();
		RestTemplate template = new RestTemplate();
		MockRestServiceServer server = MockRestServiceServer.bindTo(template).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("http://anyDataverse.com/api/v1/search?q=trees"))
		 .andExpect(method(HttpMethod.GET))
	     .andRespond(MockRestResponseCreators.withSuccess(getSimpleQueryResult(),
	    		 APPLICATION_JSON));
		SearchOperationsImplV1 tss = setupSearchOps(template);
		DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
		tss.configure(cfg);
		DataverseResponse<SearchResults> resp = tss.search(scfg);
		assertNotNull(resp.getData());
		assertEquals("trees", resp.getData().getQ());
	}
	
	@Test
	public void testComplexSearch() throws MalformedURLException {
		SearchConfig scfg = SearchConfig.builder().q("trees").build();
		RestTemplate template = new RestTemplate();
		MockRestServiceServer server = MockRestServiceServer.bindTo(template).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo("http://anyDataverse.com/api/v1/search?q=trees"))
		 .andExpect(method(HttpMethod.GET))
	     .andRespond(MockRestResponseCreators.withSuccess(getComplexQueryResult(),
	    		 APPLICATION_JSON));
		SearchOperationsImplV1 tss = setupSearchOps(template);
		DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
		tss.configure(cfg);
		DataverseResponse<SearchResults> resp = tss.search(scfg);
		assertNotNull(resp.getData());
		assertEquals("trees", resp.getData().getQ());
		SearchResults searches = resp.getData();
		assertEquals(3, searches.getTotalCount());
		assertEquals(3, searches.getCountInResponse());
		assertEquals(3, searches.getItems().size());
		assertEquals(1, searches.filterByType(SearchType.dataset).size());
		assertEquals(1, searches.filterByType(SearchType.dataverse).size());
		assertEquals(1, searches.filterByType(SearchType.file).size());
		
	}


	private SearchOperationsImplV1 setupSearchOps(RestTemplate template) {
		SearchOperationsImplV1 tss = new SearchOperationsImplV1();
		tss.setTemplate(template);
		return tss;
	}


	private String getSimpleQueryResult() {
		return TestFileUtils.getJsonFromFile("simpleQuery.json");
	}
	
	//gets 3 results: file, dataset, dataverse
	private String getComplexQueryResult() {
		return TestFileUtils.getJsonFromFile("multiTypeSearch.json");
	}

}
