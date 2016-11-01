/*
 * 
 */
package com.researchspace.dataverse.http;

import static com.researchspace.dataverse.entities.facade.DatasetTestFactory.createFacade;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetVersion;
import com.researchspace.dataverse.entities.Dataverse;
import com.researchspace.dataverse.entities.DataverseObject;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.entities.PublishedDataset;
import com.researchspace.dataverse.entities.Version;
import com.researchspace.dataverse.entities.facade.DatasetFacade;


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

public class DatasetOperationsTest extends AbstractIntegrationTest {
	
	@Before
	public void setup() throws Exception {
		super.setUp();
	}	

	@Test
	public void testListDatasets() {
		List<DataverseObject> results = dataverseOps.getDataverseContents(dataverseAlias);
		assertTrue(results.size() > 0);
		URL url = results.get(0).getPersistentUrl();
		assertEquals("http", url.getProtocol());
	}

	@Test
	public void testPostGetDeleteDataset() throws IOException, InterruptedException, URISyntaxException {
		DatasetFacade facade = createFacade();
		//create a new, unpublished Dataverse
		String newAlias = RandomStringUtils.randomAlphabetic(10);
    	Dataverse toCreate = DataverseOperationsTest.createADataverse(newAlias);
    	Dataverse newDV = dataverseOps.createNewDataverse(dataverseAlias, toCreate).getData();
		
		// create Dataset in child dataverse
		Identifier datasetId = dataverseOps.createDataset(facade, newDV.getAlias());
		assertNotNull(datasetId.getId());
		Dataset ds = datasetOps.getDataset(datasetId);
		String doiId = ds.getDoiId().get();
		datasetOps.uploadFile(doiId, getTestFile());
		
		//publishing will fail, as parent DV is not published
		DataverseResponse<PublishedDataset> response = datasetOps.publishDataset (datasetId, Version.MAJOR);
		assertNull(response.getData());
		assertNotNull(response.getMessage());
		
		facade.setTitle("Updated title2");
		datasetOps.updateDataset(facade, datasetId);
		List<DatasetVersion> versions = datasetOps.getDatasetVersions(datasetId);
		assertEquals(1, versions.size()); // published and draft
		assertEquals("DRAFT", versions.get(0).getVersionState());
		String msg = datasetOps.deleteDataset(datasetId).getMessage();
		dataverseOps.deleteDataverse(newAlias);
		assertNotNull(msg);
		
	}

	private File getTestFile() {
		return new File("src/test/resources/ResizablePng.zip");
	}
}
