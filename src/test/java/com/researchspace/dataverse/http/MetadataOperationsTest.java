package com.researchspace.dataverse.http;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestClientException;

import com.researchspace.dataverse.entities.DVField;
import com.researchspace.dataverse.entities.MetadataBlock;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MetadataOperationsTest  extends AbstractIntegrationTest {

	private static final int MIN_BLOCK_SIZE = 6;

	@Before
	public void setup() throws Exception {
		super.setUp();
	}

	@Test
	public void testGetMetdataBlockInfo() throws IOException {
		// metadat block size is variable
		assertTrue( metadataOPs.getMetadataBlockInfo().size() >= MIN_BLOCK_SIZE);
	}

	@Test(expected = RestClientException.class)
	public void testGetMetdataByIdBlockInfoWithInvalidId() throws IOException {
		metadataOPs.getMetadataById("abcde");
	}

	@Test
	public void testGetMetdataByIdBlockInfo() throws IOException {
		MetadataBlock block = metadataOPs.getMetadataById("biomedical");
		for (DVField fld : block.getFields().values()) {
			log.info(fld.toString());
		}
	}
}
