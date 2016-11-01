/*
 * 
 */
package com.researchspace.dataverse.http;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.search.entities.Item;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchResults;
import com.researchspace.dataverse.search.entities.SortBy;
import com.researchspace.dataverse.search.entities.SortOrder;
/**
 * /** <pre>
Copyright 2016 ResearchSpace

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
</pre>

 * The results of these tests will vary depnding on the Dataverse instance used for testing.
 * @author rspace
 *
 */
public class SearchOperationsIntegrationTest extends AbstractIntegrationTest {

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
		DataverseResponse<SearchResults<Item>> results = searchOps.search(cfg);
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
		DataverseResponse<SearchResults<Item>> results = searchOps.search(cfg);
		assertNotNull(results.getMessage(), results.getData());		
	}

}
