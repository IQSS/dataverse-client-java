package com.researchspace.dataverse.http;

import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.search.entities.SearchType;
import com.researchspace.dataverse.search.entities.SortBy;
import com.researchspace.dataverse.search.entities.SortOrder;

public class SearchURLBuilderTest {
	
	SearchURLBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new SearchURLBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildSearchUrlTypes() {
		SearchConfig cfg = SearchConfig.builder()
		 .q("any")
		 .type(EnumSet.of(SearchType.dataset, SearchType.file))
		 .build();		
		String url = builder.buildSearchUrl("/any", cfg);
		assertTrue(url, url.contains("type=dataset&type=file"));
	}

	@Test
	public void testBuildSearchUrlSort() {
		SearchConfig cfg = SearchConfig.builder()
		 .q("any")
		 .sortBy(SortBy.name)
		 .build();		
		String url = builder.buildSearchUrl("/any", cfg);
		assertTrue(url, url.contains("sort=name"));
	}
	
	@Test
	public void testBuildSearchUrlOrder() {
		SearchConfig cfg = SearchConfig.builder()
		 .q("any")
		 .sortOrder(SortOrder.asc)
		 .build();		
		String url = builder.buildSearchUrl("/any", cfg);
		assertTrue(url, url.contains("order=asc"));
	}
	
	@Test
	public void testBuildSearchUrlStart() {
		SearchConfig cfg = SearchConfig.builder()
		 .q("any")
		 .start(10)
		 .build();		
		String url = builder.buildSearchUrl("/any", cfg);
		assertTrue(url, url.contains("start=10"));
	}
	
	@Test
	public void testBuildSearchUrlPerPage() {
		SearchConfig cfg = SearchConfig.builder()
		 .q("any")
		 .perPage(5)
		 .build();		
		String url = builder.buildSearchUrl("/any", cfg);
		assertTrue(url, url.contains("per_page=5"));
	}
}
