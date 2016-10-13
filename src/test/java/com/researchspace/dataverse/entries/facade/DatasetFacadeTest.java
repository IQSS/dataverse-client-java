package com.researchspace.dataverse.entries.facade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.facade.DatasetFacade;

public class DatasetFacadeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void testAuthorRequired() {
		DatasetFacade.builder().contact(DatasetTestFactory.buildAContact()).build();
	}
	
	@Test(expected = NullPointerException.class)
	public void testContactRequired() {
		DatasetFacade.builder().author(DatasetTestFactory.buildAnAuthor()).build();
	}

}
