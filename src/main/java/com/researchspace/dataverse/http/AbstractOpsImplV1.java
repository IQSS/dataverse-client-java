package com.researchspace.dataverse.http;

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
@Slf4j
public abstract class AbstractOpsImplV1 {
	
	String apiKey = "";
	String serverURL = "";
	String serverAPIURL = serverURL +"/api";
	String serverAPIv1URL = serverAPIURL +"/v1";

	final String apiHeader = "X-Dataverse-key";
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

}
