/*
 * 
 */
package com.researchspace.dataverse.http;

import com.researchspace.dataverse.entities.DVField;
import com.researchspace.dataverse.entities.MetadataBlock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
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
@Slf4j
public class MetadataOperationsTest  extends AbstractIntegrationTest {

	private static final int MIN_BLOCK_SIZE = 6;

	@Before
	public void setup() throws Exception {
		super.setUp();
	}

	@Test
	public void testGetMetdataBlockInfo() throws IOException {
		// metadat block size is variable
		assertTrue( metadataOPs.getMetadataBlockInfo().size() >= MIN_BLOCK_SIZE);
	}

	@Test(expected = RestClientException.class)
	public void testGetMetdataByIdBlockInfoWithInvalidId() throws IOException {
		metadataOPs.getMetadataById("abcde");
	}

	@Test
	public void testGetMetdataByIdBlockInfo() throws IOException {
		MetadataBlock block = metadataOPs.getMetadataById("biomedical");
		for (DVField fld : block.getFields().values()) {
		//	log.info(fld.toString());
		}
	}
}
