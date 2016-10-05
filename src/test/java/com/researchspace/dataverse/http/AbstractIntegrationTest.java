package com.researchspace.dataverse.http;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.researchspace.dataverse.api.v1.DatasetOperations;
import com.researchspace.dataverse.api.v1.DataverseAPI;
import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.api.v1.DataverseOperations;
import com.researchspace.dataverse.api.v1.MetadataOperations;
import com.researchspace.dataverse.spring.config.DataverseSpringConfig;

import lombok.extern.slf4j.Slf4j;

@TestPropertySource(locations = "classpath:/test.properties")
@ContextConfiguration(classes = { DataverseSpringConfig.class })
@Slf4j
public class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

	DatasetOperations datasetOps;
	DataverseOperations dataverseOps;
	MetadataOperations metadataOPs;
	@Autowired
	DataverseAPI dataverseAPI;
	@Value("#{systemProperties['dataverseAlias']}")
	protected String dataverseAlias;
	@Value("#{systemProperties['dataverseApiKey']}")
	protected String apiKey;
	@Value("#{systemProperties['dataverseServerURL']}")
	protected String serverURL;

	// defaults from test.properties
	@Value("${dataverseAlias}")
	protected String dataverseAliasdefault;
	@Value("${dataverseServerURL}")
	protected String serverURLDevault;

	protected static final String ERROR_MSG = "ERROR";

	public void setUp() throws Exception {
		validateServerCredentials();
		log.info("serverURL: [{}], apiKey: [{}], dataverseId=[{}]", serverURL, apiKey, dataverseAlias);
		URL uri = new URL(serverURL);
		DataverseConfig cfg = new DataverseConfig(uri, apiKey, dataverseAlias);
		dataverseAPI.configure(cfg);
		datasetOps = dataverseAPI.getDatasetOperations();
		dataverseOps = dataverseAPI.getDataverseOperations();
		metadataOPs = dataverseAPI.getMetadataOperations();
	}

	private void validateServerCredentials() {
		Validate.notEmpty(apiKey, "ApiKey must be set via command line -DdataverseApiKey option");
		dataverseAlias = (StringUtils.isEmpty(dataverseAlias)) ? dataverseAliasdefault : dataverseAlias;
		serverURL = (StringUtils.isEmpty(serverURL)) ? serverURLDevault : serverURL;
		Validate.notEmpty(dataverseAlias,
				"Dataverse alias must be set via command line -DdataverseAlias option or in test.properties");
		Validate.notEmpty(serverURL,
				"Dataverse server URL must be set via command line -DdataverseServerURL option or in test.properties)");
	}

	@Test
	public void test() {
		assertTrue(1 == 1);// stop complaints about no methods
	}

}
