package com.researchspace.dataverse.http;

import static com.researchspace.dataverse.entities.facade.DatasetTestFactory.createFacade;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.DatasetVersion;
import com.researchspace.dataverse.entities.DataverseObject;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.entities.Version;
import com.researchspace.dataverse.entities.facade.DatasetFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		Identifier id = dataverseOps.createDataset(facade, dataverseAlias);
		assertNotNull(id.getId());
		Dataset ds = datasetOps.getDataset(id);
		String doiId = ds.getDoiId().get();
		datasetOps.uploadFile(doiId, getTestFile());
		datasetOps.publishDataset (id, Version.MAJOR);
		
		facade.setTitle("Updated title2");
		datasetOps.updateDataset(facade, id);
		List<DatasetVersion> versions = datasetOps.getDatasetVersions(id);
		assertEquals(2, versions.size()); // published and draft
		assertEquals("RELEASED", versions.get(1).getVersionState());
		String msg = datasetOps.deleteDataset(id).getMessage();
		assertNotNull(msg);
		log.debug(msg);
	}

	private File getTestFile() {
		return new File("src/test/resources/ResizablePng.zip");
	}
}
