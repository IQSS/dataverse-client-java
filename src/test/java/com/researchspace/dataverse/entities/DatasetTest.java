/*
 * 
 */
package com.researchspace.dataverse.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class DatasetTest {

	private static final String EXPECTED_DOI_ID = "10.5072/FK2/TGM6D5";
	private static final String EXAMPLE_DOI_URL = "http://dx.doi.org/10.5072/FK2/TGM6D5";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void GetDoiId() throws MalformedURLException {
		Dataset ds = new Dataset();
		assertFalse(ds.getDoiId().isPresent());
		ds.setPersistentUrl(new URL(EXAMPLE_DOI_URL));
		assertEquals(EXPECTED_DOI_ID, ds.getDoiId().get());
	}
	@Test
	public void testJacksonBehaviour() throws IOException {
		Dataset dataverseDataset = new Dataset();
		dataverseDataset.setId(1L);
		dataverseDataset.setPersistentUrl(new URL("http://localhost:8080/api/datasets/1/versions/1.1"));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jdk8Module());
		String json = "";
			json = objectMapper.writeValueAsString(dataverseDataset);
			System.out.println(json);
	}

}
