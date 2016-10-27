package com.researchspace.dataverse.entities.facade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.researchspace.dataverse.entities.facade.DatasetAuthor;

public class DatasetAuthorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = NullPointerException.class)
	public void testNameRequired() {
		DatasetAuthor.builder().authorAffiliation("somewhere").build();
	}

}
