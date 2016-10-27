package com.researchspace.dataverse.entities.facade;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.icu.util.Calendar;
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
	
	@Test
	public void testProductionDate() {
		DatasetFacade facade = new DatasetFacade() ;
		assertNull(facade.getProductionDate());
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		long nowMillis = now.getTime();
		facade.setProductionDate(now);
		now.setTime(10000L);
		
		assertEquals(nowMillis, facade.getProductionDate().getTime());
		
	}

}
