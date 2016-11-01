/*
 * 
 */
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
/** <pre>
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
*/
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
