/*
 * 
 */
package com.researchspace.dataverse.search;

import com.researchspace.dataverse.search.entities.SearchConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.researchspace.dataverse.search.entities.SearchConfig.MAX_RESULTS_PER_PAGE;
import static org.junit.Assert.assertEquals;
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
