package com.researchspace.dataverse.search;

import static com.researchspace.dataverse.search.entities.SearchConfig.MAX_RESULTS_PER_PAGE;
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
		final int MORE_THAN_MAX = 1000000;
		SearchConfig cfg = SearchConfig.builder().q("any").perPage(MORE_THAN_MAX).build();
		assertEquals(MAX_RESULTS_PER_PAGE, cfg.getPerPage());
	}

}
