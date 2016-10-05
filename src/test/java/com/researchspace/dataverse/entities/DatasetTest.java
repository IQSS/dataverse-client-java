package com.researchspace.dataverse.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
