package com.researchspace.dataverse.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.api.v1.SearchConfig;

public class SearchOperationsTest extends AbstractIntegrationTest {

	private static final String NEW_TEXT = "Do you want to publish";

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicSearchByText() {
		SearchConfig cfg = searchOps.builder().q("documentSchema.xsd").build();
		searchOps.search(cfg);
		
	}

}
