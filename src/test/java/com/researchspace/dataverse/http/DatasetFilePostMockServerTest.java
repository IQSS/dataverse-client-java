/*
 * 
 */
package com.researchspace.dataverse.http;

import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.entities.DatasetFileList;
import com.researchspace.dataverse.entities.Identifier;
import com.researchspace.dataverse.search.entities.SearchConfig;
import com.researchspace.dataverse.testutils.TestFileUtils;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

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
public class DatasetFilePostMockServerTest {


	@Test
	public void testNativeFilePost() throws MalformedURLException {
		RestTemplate template = new RestTemplate();
		DataverseOperationsImplV1 tss = setupDataverseOps(template);
		final String persistentid = "doi://dsfh.dsdsd.sds";
		setUpServerResponse(template, "http://anyDataverse.com/api/v1/datasets/:persistentId/add?persistentId="+persistentid,
				getDataSetFileUploadResults() );
		
		DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
		tss.configure(cfg);
		Identifier id = new Identifier();
		id.setId(1234L);
		id.setPersistentId(persistentid);
		DatasetFileList resp = tss.uploadNativeFile(new byte []{}, FileUploadMetadata.builder().build(), id,  "any");
		assertNotNull(resp.getFiles());
		assertEquals(1, resp.getFiles().size());
	}

	private void setUpServerResponse(RestTemplate template, String url, String response) {
		MockRestServiceServer server = MockRestServiceServer.bindTo(template).build();
		server.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url))
		 .andExpect(method(HttpMethod.POST))
	     .andRespond(MockRestResponseCreators.withSuccess(response,
	    		 APPLICATION_JSON));
	}

	DataverseOperationsImplV1  setUpDataset  (SearchConfig srchCfg, String url, GetJson expectedJsonGetter) throws MalformedURLException {
		RestTemplate template = new RestTemplate();
		DataverseOperationsImplV1 tss = setupDataverseOps(template);
		setUpServerResponse(template, url, expectedJsonGetter.getJson() );		
		DataverseConfig cfg = new DataverseConfig(new URL("http://anyDataverse.com"), "any", "alias");
		tss.configure(cfg);	
		return tss;
	}

	private DataverseOperationsImplV1 setupDataverseOps(RestTemplate template) {
		DataverseOperationsImplV1 tss = new DataverseOperationsImplV1();
		tss.setTemplate(template);
		return tss;
	}
	
	@FunctionalInterface
	static interface GetJson {
		String getJson ();
	}

	private String getDataSetFileUploadResults() {
		return TestFileUtils.getJsonFromFile("nativeFileUploadResponse.json");
	}


}
