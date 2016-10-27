package com.researchspace.dataverse.entities.facade;

import static com.researchspace.dataverse.entities.facade.DatasetTestFactory.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

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
	public void test() throws JsonProcessingException, MalformedURLException, URISyntaxException {
		DatasetFacade facade = createFacade();
		ObjectWriter mapper = new ObjectMapper().writerWithDefaultPrettyPrinter();
		Dataset dversion = builder.build(facade);
	    String json =   mapper.writeValueAsString(dversion);
	    System.out.println(json);		
	}
}
