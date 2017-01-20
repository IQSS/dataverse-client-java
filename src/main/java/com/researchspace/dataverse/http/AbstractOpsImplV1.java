/*
 * 
 */
package com.researchspace.dataverse.http;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.researchspace.dataverse.api.v1.DataverseConfig;
import com.researchspace.dataverse.entities.DataverseResponse;
import com.researchspace.springrest.ext.LoggingResponseErrorHandler;
import com.researchspace.springrest.ext.RestUtil;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public abstract class AbstractOpsImplV1 {
	
	String apiKey = "";
	String serverURL = "";
	String serverAPIURL = serverURL +"/api";
	String serverAPIv1URL = serverAPIURL +"/v1";
	protected RestTemplate template;

	public AbstractOpsImplV1() {
		super();
		this.template = createTemplate();
	}

	protected void setTemplate(RestTemplate template) {
		this.template = template;
	}

	public final static String apiHeader = "X-Dataverse-key";
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
		this.serverAPIURL = serverURL + "/api";
		this.serverAPIv1URL = this.serverAPIURL +"/v1";
	}
	
	public void configure(DataverseConfig config) {
		setApiKey(config.getApiKey());
		setServerURL(config.getServerURL().toString());		
	}
	
	 <T> void handleError(ResponseEntity<DataverseResponse<T>> resp) {
		log.debug("{}", resp.getBody());
		if (RestUtil.isError(resp.getStatusCode())) {
			String msg = String.format("Error  code returned %d with message [%s]", resp.getStatusCodeValue(),
					resp.getBody().getMessage());
			log.error(msg);
			throw new RestClientException(msg);
		}
	}

	RestTemplate createTemplate() {
		RestTemplate template = new RestTemplate();
		template.setErrorHandler(new LoggingResponseErrorHandler());
		return template;
	}
	
	String createV1Url(String ... pathComponents) {
		String url = serverAPIv1URL + "/" + StringUtils.join(pathComponents, "/") ;
		log.info("URL is {}", url);
		return url;
	}
   
	String createAdminUrl(String ... pathComponents) {
		String url = serverAPIURL + "/" + StringUtils.join(pathComponents, "/") ;
		log.info("URL is {}", url);
		return url;
	}
	
    HttpHeaders addAPIKeyToHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add(apiHeader, apiKey);
		return headers;
	}

	void nativeUpload(String doi, File file) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
