/*
 * 
 */
package com.researchspace.dataverse.http;

import com.researchspace.dataverse.api.v1.*;
import com.researchspace.dataverse.spring.config.DataverseSpringConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.net.URL;

import static org.junit.Assert.assertTrue;
/** <pre>
Copyright 2016 ResearchSpace

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
</pre>
*/
@TestPropertySource(locations = "classpath:/test.properties")
@ContextConfiguration(classes = { DataverseSpringConfig.class })
@Slf4j
public class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

	DatasetOperations datasetOps;
	DataverseOperations dataverseOps;
	MetadataOperations metadataOPs;
	InfoOperations infoOps;
	SearchOperations searchOps;
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
	//	log.info("serverURL: [{}], apiKey: [{}], dataverseId=[{}]", serverURL, apiKey, dataverseAlias);
		URL uri = new URL(serverURL);
		DataverseConfig cfg = new DataverseConfig(uri, apiKey, dataverseAlias);
		dataverseAPI.configure(cfg);
		datasetOps = dataverseAPI.getDatasetOperations();
		dataverseOps = dataverseAPI.getDataverseOperations();
		metadataOPs = dataverseAPI.getMetadataOperations();
		infoOps = dataverseAPI.getInfoOperations();
		searchOps = dataverseAPI.getSearchOperations();
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
