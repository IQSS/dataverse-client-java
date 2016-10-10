package com.researchspace.dataverse.http;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchResults;
import com.researchspace.dataverse.search.entities.SortBy;
import com.researchspace.dataverse.search.entities.SortOrder;
/**
 * The results of these tests will vary depnding on the Dataverse instance used for testing.
 * @author rspace
 *
 */
public class SearchOperationsTest extends AbstractIntegrationTest {

	private static final String FILE_SEARCH_TERM = "documentSchema.xsd";
	private static final String NEW_TEXT = "Do you want to publish";

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicSearchByTermOnly() {
		SearchConfig cfg = searchOps.builder().q(FILE_SEARCH_TERM).build();
		DataverseResponse<SearchResults> results = searchOps.search(cfg);
		assertNotNull(results.getMessage(), results.getData());		
	}
	
	@Test
	public void testComplexSearch() {
		SearchConfig cfg = searchOps.builder().q(FILE_SEARCH_TERM)
				.sortBy(SortBy.date)
				.sortOrder(SortOrder.asc)
				.showFacets(true)
				.showRelevance(true)
				.start(1)
				.perPage(3)
				.build();
		DataverseResponse<SearchResults> results = searchOps.search(cfg);
		assertNotNull(results.getMessage(), results.getData());		
	}

}
