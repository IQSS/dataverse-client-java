/*
 * 
 */
package com.researchspace.dataverse.http;

import com.researchspace.dataverse.entities.*;
import com.researchspace.dataverse.entities.facade.DatasetFacade;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static com.researchspace.dataverse.entities.facade.DatasetTestFactory.createFacade;
import static org.junit.Assert.*;


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

	File exampleDatasetJson = new File("src/integration-test/resources/dataset-create-new-all-default-fields.json");
	@Test
	public void testListDatasets() {
		List<DataverseObject> results = dataverseOps.getDataverseContents(dataverseAlias);
		assertTrue(results.size() > 0);

	}

	//TODO figure out why data is invalid
	@Test
	@Ignore("this test fails with message: Error parsing Json: incorrect multiple   for field collectionMode")
	public void testPostSampleDataset() throws IOException, InterruptedException, URISyntaxException {
		String toPost = FileUtils.readFileToString(exampleDatasetJson);
		Identifier datasetId = dataverseOps.createDataset(toPost, dataverseAlias);
		assertNotNull(datasetId.getId());
	}

	@Test
	public void uploadFileToDataSetWithNativeApiBytes() throws IOException, URISyntaxException {
		//arrange
		Identifier datasetId = createADataset();
		assertNotNull(datasetId.getId());
		FileUploadMetadata meta = getUploadMetadata();

		//act
		DatasetFileList datasetFileList = datasetOps.uploadNativeFile(new byte[]{1, 2, 3, 4, 5}, meta, datasetId,  "myFileName.dat");

		//assert
		assertNotNull(datasetFileList);
		assertEquals(1, datasetFileList.getFiles().size());
		assertTrue(datasetFileList.getFiles().get(0).getCategories().contains("Data"));
		assertTrue(datasetFileList.getFiles().get(0).getDescription().equals(("My description.")));
		assertEquals(5 ,datasetFileList.getFiles().get(0).getDataFile().getFilesize());
	}

	@Test
	public void uploadFileToDataSetWithNativeApiInputStream() throws IOException, URISyntaxException {
		// arrange
		Identifier datasetId = createADataset();
		assertNotNull(datasetId.getId());
		FileUploadMetadata meta = getUploadMetadata();

		//act
		DatasetFileList datasetFileList = datasetOps.uploadNativeFile(new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5,6}), 6, meta, datasetId,  "myFileName.dat");

		//assert
		assertNotNull(datasetFileList);
		assertEquals(1, datasetFileList.getFiles().size());
		DatasetFile uploadedFile = datasetFileList.getFiles().get(0);
		assertTrue(uploadedFile.getCategories().contains("Data"));
		assertTrue(uploadedFile.getDescription().equals(("My description.")));
		assertEquals(6 ,uploadedFile.getDataFile().getFilesize());
	}

	private Identifier createADataset() throws MalformedURLException, URISyntaxException {
		DatasetFacade facade = createFacade();
		Identifier datasetId = dataverseOps.createDataset(facade, dataverseAlias);
		return datasetId;
	}

	private FileUploadMetadata getUploadMetadata() {
		return FileUploadMetadata.builder().description("My description.").categories(Arrays.asList(new String[]{"Data"}))
				.directoryLabel("test/x").build();
	}

	@Test
	public void testPostGetDeleteDataset() throws IOException, InterruptedException, URISyntaxException {
		DatasetFacade facade = createFacade();
		//create a new, unpublished Dataverse
		String newAlias = RandomStringUtils.randomAlphabetic(10);
    	DataversePost toCreate = DataverseOperationsTest.createADataverse(newAlias);
    	DataversePost newDV = dataverseOps.createNewDataverse(dataverseAlias, toCreate).getData();
		
		// create Dataset in child dataverse
		Identifier datasetId = dataverseOps.createDataset(facade, newDV.getAlias());
		assertNotNull(datasetId.getId());
		assertNotNull(datasetId.getPersistentId());
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
		return new File("src/integration-test/resources/ResizablePng.zip");
	}
}
