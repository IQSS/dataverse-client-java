package com.researchspace.dataverse.entries.facade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.researchspace.dataverse.entities.Dataset;
import com.researchspace.dataverse.entities.facade.DatasetBuilder;
import com.researchspace.dataverse.entities.facade.DatasetFacade;

public class DatasetBuilderTest {

	DatasetBuilder builder;
	@Before
	public void setUp() throws Exception {
		builder = new DatasetBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws JsonProcessingException {
		DatasetFacade facade = createFacade();
		ObjectWriter mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
		Dataset dversion = builder.build(facade);
	   String json =   mapper.writeValueAsString(dversion);
		
	}

	public static DatasetFacade createFacade() {
		return new DatasetFacade("title1", "Fred Blogs", "Som admin", "fred@blogs.com", " some desc", "Chemistry", "a Depositor");
	}

}
