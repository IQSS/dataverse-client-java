package com.researchspace.dataverse.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.api.v1.SearchConfig;

public class SearchConfigTest {
	
	SearchConfig cfg;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		SearchConfig cfg = SearchConfig.builder().q("term").build();
	}

}
