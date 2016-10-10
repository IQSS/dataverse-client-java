package com.researchspace.dataverse.search;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.search.entities.SearchConfig;

public class SearchConfigTest {

	SearchConfig cfg;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void builderRequiresNonNullSearchTerm() {
		SearchConfig.builder().build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void startCannotBeNegative() {
		SearchConfig.builder().start(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void perPageCannotBeNegative() {
		SearchConfig.builder().perPage(-1);
	}

	@Test
	public void perPageHas1000Maximum() {
		SearchConfig cfg = SearchConfig.builder().q("any").perPage(1000000).build();
		assertEquals(SearchConfig.MAX_RESULTS_PER_PAGE, cfg.getPerPage());
	}

}
