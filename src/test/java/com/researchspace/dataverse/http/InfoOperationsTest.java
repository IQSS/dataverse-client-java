package com.researchspace.dataverse.http;

import static org.junit.Assert.*;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.dataverse.entities.DvMessage;

public class InfoOperationsTest extends AbstractIntegrationTest {

	private static final String NEW_TEXT = "Do you want to publish";

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSetDatasetPublishPopupCustomText() {
		DvMessage originalText = infoOps.getDatasetPublishPopupCustomText();
		assertTrue("Message text was empty", !StringUtils.isEmpty(originalText.getMessage()));
		DataverseResponse<Object> response = infoOps.setDatasetPublishPopupCustomText(NEW_TEXT);
		assertTrue("New text was not set", response.getData().toString().contains(NEW_TEXT));
	    response = infoOps.setDatasetPublishPopupCustomText(originalText.getMessage());
	    assertTrue("Originl text was not restored", response.getData().toString().contains(originalText.getMessage()));		
	}

}
