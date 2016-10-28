package com.researchspace.dataverse.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.Dataverse;
import com.researchspace.dataverse.entities.DataverseContacts;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;

public class DataverseOperationsTest extends AbstractIntegrationTest {

	

	@Before
	public void setup() throws Exception {
		super.setUp();
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createPublishAndDeleteNewDataverse(){
		String dvName = RandomStringUtils.randomAlphabetic(10);
		Dataverse dv = createADataverse(dvName);
		DataverseResponse<Dataverse>  success = dataverseOps.createNewDataverse(dataverseAlias, dv);
		assertNotNull(success.getData());
		assertNotNull(success.getData().getId());
		
		dataverseOps.publishDataverse(dvName);
		
		DataverseResponse<DvMessage> deleted = dataverseOps.deleteDataverse(dvName);
		assertTrue(deleted.getStatus().equals("OK"));
		assertNotNull(deleted.getData());	
	}

	static Dataverse createADataverse(String dvName) {
		Dataverse dv = new Dataverse();
		dv.setAlias(dvName);
		dv.setName("Test Instance " + dvName);
		dv.setDataverseContacts(Arrays.asList(new DataverseContacts("a@b.com")));
		return dv;
	}

	@Test
	public void deleteUnknownDataverseHandled () {		
		DataverseResponse<DvMessage> deleted = dataverseOps.deleteDataverse("ra");
		assertTrue(deleted.getStatus().equals(ERROR_MSG));
		assertNull(deleted.getData());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createDataverseValidation () {		
		String dvName = RandomStringUtils.randomAlphabetic(10);
		Dataverse dv = createADataverse(dvName);
		dv.setAlias("");
		dataverseOps.createNewDataverse("rspace", dv);
	}
	@Test(expected=IllegalArgumentException.class)
	public void createDataverseValidationContactREquired () {		
		String dvName = RandomStringUtils.randomAlphabetic(10);
		Dataverse dv = createADataverse(dvName);
		dv.setDataverseContacts(null);
		dataverseOps.createNewDataverse("rspace", dv);
	}

	@Test
	public void testListDataverses() {
		Dataverse dv = dataverseOps.getDataverseById(dataverseAlias);
		assertNotNull(dv.getId());
	}
}
